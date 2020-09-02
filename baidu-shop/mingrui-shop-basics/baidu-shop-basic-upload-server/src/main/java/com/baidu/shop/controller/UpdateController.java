package com.baidu.shop.controller;

import com.baidu.shop.base.BeanApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.status.HTTPStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName UpdateController
 * @Description: TODO
 * @Author tianzhiao
 * @Date 2020/9/1
 * @Version V1.09999999999999999
 **/
@RestController
public class UpdateController extends BeanApiService {
    //linux系统的上传目录
    @Value(value = "${mingrui.upload.path.windows}")
    private String windowsPath;

    //window系统的上传目录
    @Value(value = "${mingrui.upload.path.linux}")
    private String linuxPath;

    //图片服务器的地址
    @Value(value = "${mingrui.upload.img.host}")
    private String imgHost;

    @PostMapping("/upload")
    public Result<String> uploadImg(@RequestParam("file") MultipartFile file) {

        if(file.isEmpty()) return this.setResultError("上传的文件为空");//判断上传的文件是否为空

        String filename = file.getOriginalFilename();//获取文件名

        String path = "";
        String os = System.getProperty("os.name").toLowerCase();
        if(os.indexOf("win") != -1){
            path = windowsPath;
        }else if(os.indexOf("lin") != -1){
            path = linuxPath;
        }

        filename = UUID.randomUUID() + filename;//防止文件名重复

        //创建文件 路径+分隔符(linux和window的目录分隔符不一样)+文件名
        File dest = new File(path + File.separator + filename);

        //判断文件夹是否存在,不存在的话就创建
        if(!dest.getParentFile().exists()) dest.getParentFile().mkdirs();

        try {
            file.transferTo(dest);//上传
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return this.setResult(HTTPStatus.OK,"upload success!!!",imgHost+"/" + filename);//将文件名返回页面用于页面回显
    }
}