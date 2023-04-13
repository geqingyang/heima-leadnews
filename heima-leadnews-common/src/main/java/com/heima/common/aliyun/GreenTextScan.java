package com.heima.common.aliyun;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Getter
@Setter
@Component
@PropertySource("classpath:oss.properties")
@ConfigurationProperties(prefix = "aliyun.oss")
public class GreenTextScan {

    private String accessKeyId;
    private String accessKeySecret;

    /**
     * 阿里云文本内容检查
     * @param content
     * @return map  key - suggestion  value-
     *                     pass：文本正常，可以直接放行，
     *                     review：文本需要进一步人工审核，
     *                     block：文本违规，可以直接删除或者限制公开
     *              key - reason   value -   通过，或 出错原因
     * @throws Exception
     */
    public Map<String,String> greenTextScan(String content) throws Exception {
        IClientProfile profile = DefaultProfile
                .getProfile("cn-shanghai", accessKeyId, accessKeySecret);
        DefaultProfile
                .addEndpoint("cn-shanghai", "cn-shanghai", "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // 指定api返回格式
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // 指定请求方法
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId("cn-shanghai");
        List<Map<String, Object>> tasks = new ArrayList<>();
        Map<String, Object> task1 = new LinkedHashMap<>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * 待检测的文本，长度不超过10000个字符
         */
        task1.put("content", content);
        tasks.add(task1);
        JSONObject data = new JSONObject();

        /**
         * 检测场景，文本垃圾检测传递：antispam
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);
        log.info("检测任务内容：{}",JSON.toJSONString(data, true));
        textScanRequest.setHttpContent(data.toJSONString().getBytes("UTF-8"), "UTF-8", FormatType.JSON);
        // 请务必设置超时时间
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);

//        返回结果内容
        Map<String, String> resultMap = new HashMap<>();
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if(!httpResponse.isSuccess()){
                new RuntimeException("阿里云文本内容检查出现异常！");
            }
            JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));
            log.info("检测结果内容：{}",JSON.toJSONString(scrResponse, true));
            if (200 != scrResponse.getInteger("code")) {
                new RuntimeException("阿里云文本内容检查出现异常！");
            }
            JSONArray taskResults = scrResponse.getJSONArray("data");
            for (Object taskResult : taskResults) {
                if (200 != ((JSONObject) taskResult).getInteger("code")) {
                    new RuntimeException("阿里云文本内容检查出现异常！");
                }
                JSONArray sceneResults = ((JSONObject) taskResult).getJSONArray("results");
                for (Object sceneResult : sceneResults) {
                    String scene = ((JSONObject) sceneResult).getString("scene");
                    String label = ((JSONObject) sceneResult).getString("label");
                    String suggestion = ((JSONObject) sceneResult).getString("suggestion");
                    log.info("最终内容检测结果，suggestion = {}，label={}",suggestion,label);
//                    设置默认错误返回内容
                    resultMap.put("suggestion", suggestion);
                    if (suggestion.equals("review")) {
                        resultMap.put("reson","文章内容中有不确定词汇");
                        log.info("返回结果，resultMap={}",resultMap);
                        return resultMap;
                    }else if(suggestion.equals("block")){
                        String reson = "文章内容中有敏感词汇";
                        if(label.equals("spam")){
                            reson="文章内容中含垃圾信息";
                        }else if(label.equals("ad")){
                            reson="文章内容中含有广告";
                        }else if(label.equals("politics")){
                            reson="文章内容中含有涉政";
                        }else if(label.equals("terrorism")){
                            reson="文章内容中含有暴恐";
                        }else if(label.equals("abuse")){
                            reson="文章内容中含有辱骂";
                        }else if(label.equals("porn")){
                            reson="文章内容中含有色情";
                        }else if(label.equals("flood")){
                            reson="文章内容灌水";
                        }else if(label.equals("contraband")){
                            reson="文章内容违禁";
                        }else if(label.equals("meaningless")){
                            reson="文章内容无意义";
                        }
                        resultMap.put("reason",reson);
                        log.info("返回结果，resultMap={}",resultMap);
                        return resultMap;
                    }

                }
            }
            resultMap.put("suggestion", "pass");
            resultMap.put("reson","检测通过");

        } catch (Exception e) {
            log.error("阿里云文本内容检查出错！");
            e.printStackTrace();
            new RuntimeException("阿里云文本内容检查出错！");
        }
        log.info("返回结果，resultMap={}",resultMap);
        return resultMap;
    }

}