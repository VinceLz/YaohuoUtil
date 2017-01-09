package com.xawl.car.service.impl;

import com.xawl.car.mapper.DbHomeMapper;
import com.xawl.car.mapper.DbImageMapper;
import com.xawl.car.mapper.DbModelMapper;
import com.xawl.car.pojo.DbHome;
import com.xawl.car.pojo.DbImage;
import com.xawl.car.pojo.DbModel;
import com.xawl.car.service.PictureService;
import com.xawl.car.utils.CarResult;
import com.xawl.car.utils.IDUtils;
import com.xawl.car.utils.IOUtils;
import com.xawl.car.utils.JsonUtils;
import com.xawl.car.utils.PictureResult;
import com.xawl.car.utils.PoiUtils;
import com.xawl.car.utils.SFTPTool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PictureServiceImpl
  implements PictureService
{

  @Value("${FTP_ADDRESS}")
  private String FTP_ADDRESS;

  @Value("${FTP_PORT}")
  private Integer FTP_PORT;

  @Value("${FTP_USERNAME}")
  private String FTP_USERNAME;

  @Value("${FTP_PASSWORD}")
  private String FTP_PASSWORD;

  @Value("${FTP_BASE_PATH}")
  private String FTP_BASE_PATH;

  @Value("${IMAGE_BASE_URL}")
  private String IMAGE_BASE_URL;
  private PictureResult pictureResult;

  @Autowired
  private DbImageMapper dbImageMapper;

  @Autowired
  private DbModelMapper dbModelMapper;

  @Autowired
  private DbHomeMapper dbHomeMapper;

  public Map uploadPicture(MultipartFile uploadFile, Integer id, String idName, Integer iid, HttpServletRequest request)
  {
    System.out.println(id + "_---" + idName + "_------" + iid + "-----------");
    Map resultMap = new HashMap();
    try
    {
      String oldName = uploadFile.getOriginalFilename();

      String newName = IDUtils.getImageName();
      newName = newName + oldName.substring(oldName.lastIndexOf("."));

      System.out.print("xuzi" + newName);

      ServletContext sc = request.getSession().getServletContext();
      String path = sc.getRealPath("img") + "/";
      File f = new File(path);
      if (!f.exists()) {
        f.mkdirs();
      }
      FileOutputStream fos = new FileOutputStream(path + newName);

      int b1 = 0;
      InputStream in = uploadFile.getInputStream();
      while ((b1 = in.read()) != -1) {
        fos.write(b1);
      }

      DbImage dbImage = new DbImage();
      dbImage.setImage("/img/" + newName);
      if (iid.intValue() > 0)
      {
        dbImage.setIid(iid);
        this.dbImageMapper.updateByPrimaryKeySelective(dbImage);
      }
      else {
        if (idName.equals("bid")) {
          dbImage.setBid(id);
        } else if (idName.trim().equals("gid")) {
          System.out.println("--------------------" + idName + "上海市");
          dbImage.setGid(id);
        } else if (idName.equals("mid")) {
          dbImage.setMid(id);
        }
        this.dbImageMapper.insert(dbImage);
      }
      resultMap.put("error", Integer.valueOf(0));
      resultMap.put("url", "/img/" + newName);
      return resultMap;
    } catch (Exception e) {
      e.printStackTrace();
      resultMap.put("error", Integer.valueOf(1));
      resultMap.put("message", "exception");
    }return resultMap;
  }

  public Map uploadPicture(MultipartFile uploadFile, HttpServletRequest request)
  {
    Map resultMap = new HashMap();
    try
    {
      String oldName = uploadFile.getOriginalFilename();

      String newName = IDUtils.getImageName();
      newName = newName + oldName.substring(oldName.lastIndexOf("."));

      System.out.print("xuzi" + newName);

      ServletContext sc = request.getSession().getServletContext();
      String path = sc.getRealPath("img") + "/";
      File f = new File(path);
      if (!f.exists()) {
        f.mkdirs();
      }
      FileOutputStream fos = new FileOutputStream(path + newName);

      int b1 = 0;
      InputStream in = uploadFile.getInputStream();
      while ((b1 = in.read()) != -1) {
        fos.write(b1);
      }
      fos.close();
      in.close();
      resultMap.put("error", Integer.valueOf(0));
      resultMap.put("url", "/img/" + newName);

      return resultMap;
    } catch (Exception e) {
      resultMap.put("error", Integer.valueOf(1));
      resultMap.put("message", "exception");
    }return resultMap;
  }

  public CarResult dropImage(String imageName)
  {
    try {
      SFTPTool sftpTool = new SFTPTool("172.16.120.1", 22, "root", "zhen1314");
      sftpTool.delete(this.FTP_BASE_PATH, imageName);
      return CarResult.ok();
    } catch (Exception e) {
      e.printStackTrace();
    }return CarResult.build(Integer.valueOf(500), "删除失败");
  }

  public CarResult uploadExecl(MultipartFile execlFile, Integer mid)
  {
    try
    {
      LinkedHashMap map = PoiUtils.execl2Map(execlFile.getInputStream());
      System.out.println(map);
      String s = JsonUtils.objectToJson(map);
      DbModel dbModel = new DbModel();
      dbModel.setMid(mid);
      dbModel.setConfigure(s);
      System.out.println(this.dbModelMapper);

      this.dbModelMapper.updateConfig(dbModel);
      return CarResult.ok(s);
    } catch (Exception e) {
      e.printStackTrace();
    }return CarResult.build(Integer.valueOf(500), "错误");
  }

  public Map updateUpload(MultipartFile uploadFile, String imageName, HttpServletRequest request)
  {
    Map resultMap = new HashMap();
    try
    {
      String oldName = uploadFile.getOriginalFilename();

      String newName = IDUtils.getImageName();
      newName = newName + oldName.substring(oldName.lastIndexOf("."));

      System.out.print("xuzi" + newName);

      ServletContext sc = request.getSession().getServletContext();
      String path = sc.getRealPath("img") + "/";
      File f = new File(path);
      if (!f.exists()) {
        f.mkdirs();
      }
      FileOutputStream fos = new FileOutputStream(path + newName);

      int b1 = 0;
      InputStream in = uploadFile.getInputStream();
      while ((b1 = in.read()) != -1) {
        fos.write(b1);
      }

      resultMap.put("error", Integer.valueOf(0));
      resultMap.put("url", "/img/" + newName);

      fos.close();
      in.close();
      return resultMap;
    } catch (Exception e) {
      resultMap.put("error", Integer.valueOf(1));
      resultMap.put("message", "exception");
    }return resultMap;
  }

  public Map uploadHomeImage(MultipartFile uploadFile, DbHome dbHome, HttpServletRequest request)
  {
    Map resultMap = new HashMap();
    try
    {
      String oldName = uploadFile.getOriginalFilename();

      String newName = IDUtils.getImageName();
      newName = newName + oldName.substring(oldName.lastIndexOf("."));

      System.out.print("xuzi" + newName);

      ServletContext sc = request.getSession().getServletContext();
      String path = sc.getRealPath("img") + "/";
      File f = new File(path);
      if (!f.exists()) {
        f.mkdirs();
      }
      FileOutputStream fos = new FileOutputStream(path + newName);

      int b1 = 0;
      InputStream in = uploadFile.getInputStream();
      while ((b1 = in.read()) != -1) {
        fos.write(b1);
      }
      fos.close();
      in.close();
      dbHome.setImage("/img/" + newName);
      this.dbHomeMapper.updateByPrimaryKeySelective(dbHome);
      resultMap.put("error", Integer.valueOf(0));
      resultMap.put("url", "/img/" + newName);

      return resultMap;
    } catch (Exception e) {
      resultMap.put("error", Integer.valueOf(1));
      resultMap.put("message", "exception");
    }return resultMap;
  }

  public CarResult deleteCarouselImage(Integer iid, HttpServletRequest request)
  {
    try
    {
      DbImage dbImage = this.dbImageMapper.selectByPrimaryKey(iid);
      String image = dbImage.getImage();
      if (!TextUtils.isEmpty(image)) {
        String[] split = image.split("/");
        ServletContext sc = request.getSession().getServletContext();
        String path = sc.getRealPath("img") + "/";
        path = path + split[(split.length - 1)];
        System.out.print("xuzi:++++++++++++" + path);
        IOUtils.deleteLocalFile(path);
      }
      this.dbImageMapper.deleteByPrimaryKey(iid);
      return CarResult.ok();
    } catch (Exception e) {
      e.printStackTrace();
    }return CarResult.build(Integer.valueOf(500), "失败");
  }
}