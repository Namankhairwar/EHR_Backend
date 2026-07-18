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
//        folder("C:\\Users\\Krishana dubey\\OneDrive\\Desktop\\EHR_Backend\\src\\main\\java\\com\\clinic\\patient","dto","controller","entity","state");
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
//
//////package com.clinic.patient;
//////import java.util.Arrays;
//////import java.util.Stack;
//////
//////public class Main{
//////
//////public static void main(String[] args) {
//////    System.out.println(Arrays.toString(arr(new int[]{16,17,4,3,5,2})));
//////}
//////public static  Integer[] arr(int[] ar){
//////    Stack<Integer> o =new Stack<Integer>();
//////    for (int i = 0; i < ar.length; i++) {
//////        if(o.isEmpty())o.push(ar[i]);
//////        else {
//////            while (!o.isEmpty() &&o.peek()<ar[i])o.pop();
//////            o.push(ar[i]);
//////        }
//////    }
//////    Integer[] ap = new Integer[o.size()];
//////    int i = 0;
//////    while(!o.isEmpty()){
//////        ap[i]=o.pop();
//////        i++;
//////    }
//////    int l =0;
//////    int h= ap.length-1;
//////    while(l<h){
//////        int ll=ap[l];
//////        ap[l] = ap[h];
//////        ap[h] = ll;
//////        l++;
//////        h--;
//////    }
//////    return ap;
//////}
//////        }