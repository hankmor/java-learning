package com.belonk.io.file;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sun on 2017/9/20.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class Test {
    //~ Static fields/initializers =====================================================================================


    //~ Instance fields ================================================================================================
    private static volatile int idx = 0;
    //~ Constructors ===================================================================================================


    //~ Methods ========================================================================================================
    public static void main(String[] args) throws Exception {
        System.out.println();
        ArrayList<SpecialPlanetTcketVo> TcketVosList = new ArrayList<>();//特价机票的信息
        ArrayList<SectionVo> sectionList = new ArrayList();//使用航班的区间信息
        FileLineReader.Builder builder = new FileLineReader.Builder("D:\\work\\99-其他\\机票\\dataNFD_20170920.txt", line -> {
            // 逻辑
//            if (line.matches("^[a-z|A-Z].*")) {
//
//            } else {
                System.out.println(line);
//            }
            DateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            proccessLine(df, TcketVosList, sectionList, line);
        });
        builder.withTreahdSize(10).withCharset("gbk").withBufferSize(1024 * 1024);
        FileLineReader bigFileReader = builder.build();
        bigFileReader.start();
//        bigFileReader.shutdown();
    }

    private static void proccessLine(DateFormat sdf, ArrayList<SpecialPlanetTcketVo> tcketVosList, ArrayList<SectionVo> sectionList, String line) {
        try {
            SpecialPlanetTcketVo specialPlanetTcketVo = new SpecialPlanetTcketVo();
            String[] split = line.split("\\|", 16);
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            specialPlanetTcketVo.setId(uuid);
            specialPlanetTcketVo.setDeparture_city(split[0]);//出发城市
            specialPlanetTcketVo.setArrival_city(split[1]);//抵达城市
            specialPlanetTcketVo.setAirline_company(split[2]);//航空公司
            specialPlanetTcketVo.setShipping_code(split[3]);//舱位代码
            Date travel_date_start = sdf.parse(split[4]);//旅行日期起始
            specialPlanetTcketVo.setTravel_date_start(travel_date_start);//旅行日期起始
            Date travel_date_cutoff = sdf.parse(split[5]);//旅行日期截止
            specialPlanetTcketVo.setTravel_date_cutoff(travel_date_cutoff);//旅行日期截止
            Date start_date_of_sale = sdf.parse(split[6]);//销售日期起始
            specialPlanetTcketVo.setStart_date_of_sale(start_date_of_sale);//销售日期起始
            Date closing_date = sdf.parse(split[7]);//销售日期截止
            specialPlanetTcketVo.setClosing_date(closing_date);//销售日期截止
            specialPlanetTcketVo.setFace_value(split[8]);//票面价
            specialPlanetTcketVo.setFare_basis(split[9]);//运价基础
            specialPlanetTcketVo.setEarliest_advance_ticket(split[10]);//提前出票最早
            specialPlanetTcketVo.setLatest_ticket(split[11]);//提前出票最晚
            specialPlanetTcketVo.setTime_limit(split[12]);//班期限制
            String qj = split[13];
            String[] qjjx = qj.split("/");
            /**
             * 以下对使用航班的区间进行处理
             */
            if (!"".equals(qj)) {
                for (int j = 0; j < qjjx.length; j++) {
                    String section = qjjx[j];
                    String[] splitArry = section.split("-");
                    int start_section = Integer.parseInt(splitArry[0]);
                    int end_section = Integer.parseInt(splitArry[1]);
                    String sectionUuid = UUID.randomUUID().toString().replaceAll("-", "");
                    SectionVo sectionVo = new SectionVo();
                    sectionVo.setTicketId(uuid);
                    sectionVo.setId(sectionUuid);
                    sectionVo.setStart_section(start_section);
                    sectionVo.setEnd_section(end_section);
                    sectionList.add(sectionVo);
                }
            }
            //            specialPlanetTcketVoMapper.saveSectionINfo(sectionList);
            sectionList.clear();
            specialPlanetTcketVo.setFlight_availability(split[13]);//适用航班
            specialPlanetTcketVo.setNot_applicable_flight(split[14]);//航班不适用
            specialPlanetTcketVo.setApplicable_time_range(split[15]);//适用时刻范围
            tcketVosList.add(specialPlanetTcketVo);
            idx++;
            if (idx % 1000 == 0) {
//                    specialPlanetTcketVoMapper.insert(TcketVosList);//插入到数据库
                System.out.println("数据入库……");
                Thread.sleep(3000);
                tcketVosList.clear();//清空集合
                idx = 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException : " + line);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException : " + line);
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(line);
            e.printStackTrace();
        }
    }
}
