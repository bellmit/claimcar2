/*package ins.sino.claimcar.hnbxrest;

import ins.sino.claimcar.hnbxrest.util.EncryptUtils;
import ins.sino.claimcar.hnbxrest.vo.ReceiveauditingresultVo;
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class Test {
    
    public static void main(String[] ar) throws Exception {
        File file = new File("C:\\install.log");
        FileInputStream InputStream = new FileInputStream(file);
        
        byte[] bytes = IOUtils.toByteArray(InputStream);
        ObjectMapper mapper = new ObjectMapper();
        String my = mapper.writeValueAsString(bytes);
        JSONObject json = new JSONObject();
        json.put("imagebytes",my);
        json.put("filesuffix",".log");
        String params = json.toString();
        HttpPost httpPost = new HttpPost("http://220.231.252.128:8048/restservices/hnbxrest/receiveauditingresult/query");
        if(params!=null&& !params.trim().equals("")){
            StringEntity requestEntity = new StringEntity(params,ContentType.create("application/json","UTF-8"));
            httpPost.setEntity(requestEntity);
        }
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity httpEntity = response.getEntity();
        String content = EntityUtils.toString(httpEntity,"UTF-8");
        JSONObject rejson = JSONObject.fromObject(content);
        System.out.println(rejson.toString());
        int restate = rejson.getInt("restate");
        System.out.println(restate);
        String redes = rejson.getString("redes");
        System.out.println(redes);
        EncryptUtils encryptUtils = new EncryptUtils();
        ReceiveauditingresultVo receiveauditingresultVo = new ReceiveauditingresultVo();
        try{
            private String casecarno;//???????????????
            private String inscaseno;//???????????????
            private String datatype;//?????????????????? 0-?????????????????? 1-??????????????????????????????
            private String isqualify;//?????????????????? 0-????????? 1-??????
            private String imagetype;//???????????? 11-?????????????????? 12-??????????????????
            private String isass;//?????????????????????????????? 0-????????? 1-??????
            private String username;//????????? ??????1??????????????????????????????
            private String password;//?????? 888888a            
            JSONObject json = new JSONObject();
            json.put("casecarno",receiveauditingresultVo.getCasecarno());
            json.put("inscaseno",receiveauditingresultVo.getInscaseno());
            json.put("datatype",receiveauditingresultVo.getDatatype());
            json.put("isqualify",receiveauditingresultVo.getIsqualify());
            json.put("imagetype",receiveauditingresultVo.getImagetype());
            json.put("isass",receiveauditingresultVo.getIsass());
            json.put("username",receiveauditingresultVo.getUsername());
            json.put("password",EncryptUtils.encodeStr(receiveauditingresultVo.getPassword()));
            //json.put("casecarno",receiveauditingresultVo.getCasecarno());
            json.put("inscaseno","4000200201712060020483");
            json.put("datatype","0");
            json.put("isqualify","1");
            json.put("imagetype","11");
            json.put("isass","1");
            json.put("username","410800003043");
            json.put("password",encryptUtils.desEncrypt("888888a"));
            String params = json.toString();
            HttpPost httpPost = new HttpPost("http://220.231.252.128:8048/restservices/hnbxrest/receiveauditingresult/query");
            if(params!=null&& !params.trim().equals("")){
                StringEntity requestEntity = new StringEntity(params,ContentType.create("application/json","UTF-8"));
                httpPost.setEntity(requestEntity);
            }
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            String content = EntityUtils.toString(httpEntity,"UTF-8");
            JSONObject rejson = JSONObject.fromObject(content);
            System.out.println(rejson.toString());
            int restate = rejson.getInt("restate");
            System.out.println(restate);
            String redes = rejson.getString("redes");
            System.out.println(redes);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
*/