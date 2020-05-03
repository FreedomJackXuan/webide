package test;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;

import java.io.File;

public class OSSDownload {
    public static void main(String[] args) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI4GARAqx1zGgEkBFZMmS2";
        String accessKeySecret = "UYXA9blEZVX7PrzFgaf8xuWtJT7fpy";
        String bucketName = "ide-zl";
        String objectName = "13000000000/a.py";

// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建。
        ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File("/home/jac/IdeaProjects/bbb/"+objectName));

// 关闭OSSClient。
        ossClient.shutdown();
    }
}
