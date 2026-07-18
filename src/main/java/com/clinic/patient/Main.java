//package com.clinic.patient;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.util.ArrayList;
//import java.util.List;
//
//public class Main {
//
//    public static void main(String[] args) throws IOException {
//        folder("C:\\Users\\Admin\\Desktop\\project\\EHR_Backend\\src\\main\\java\\com\\clinic\\patient","dto","controller","entity");
//    }
//
//    public static void folder(String d , String... path) throws IOException {
//      List<String> dp = new ArrayList<>();
//      List<String> df=  new ArrayList<>();
//       dp.add(d);
//      while(!dp.isEmpty()){
//          int size= dp.size();
//          while(size-->0) {
//              String cur = dp.removeFirst();
//              File file = new File(cur);
//              System.out.println(file.exists());
//
//              String[] ps = file.list();
//              if (ps != null) {
//                  for (String j : ps) {
//                      boolean f = false;
//                      for (String dm : path) {
//                          if (j.endsWith(dm)) {
//                              df.addLast(cur + "\\" + j);
//                              f = true;
//                          }
//                      }
//                      if (!f) dp.addLast(cur + "\\" + j);
//                  }
//
//              }
//          }
//      }
//int size= df.size();
//      while(size-->0){
//          File fike= new File(df.removeFirst());
//        if(fike.isFile()){
//            df.addLast(fike.getAbsolutePath());
//        }else{
//            folderSearch(df,fike.getAbsolutePath());
//        }
//
//      }
//        System.out.println(df);
//      File file=  new File(d+"\\"+"file.txt");
//      file.createNewFile();
//      while(!df.isEmpty()){
//          Files.write(Paths.get(d+"\\"+"file.txt"),Files.readAllBytes(Path.of(df.removeFirst())), StandardOpenOption.APPEND);
//      }
//    }
//
//    public static void folderSearch(List<String> ans,String path){
//
//        File file =new File(path);
//        if(file.isFile()){
//            ans.addLast(file.getAbsolutePath());
//        }else {
//            String[] pa= file.list();
//            for(String h:pa){
//                folderSearch(ans,path+"\\"+h);
//
//            }
//        }
//    }
//}
