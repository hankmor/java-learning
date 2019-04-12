package com.belonk.json.jackson;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by sun on 2019/4/11.
 *
 * @author sunfuchang03@126.com
 * @version 1.0
 * @since 1.0
 */
public class JacksonBasisDemo {
    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Static fields/constants/initializer
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Instance fields
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Constructors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Public Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static void main(String[] args) throws Exception {
        JacksonBasisDemo demo = new JacksonBasisDemo();
        demo.runDemo1();
        System.out.println("==========================");
        demo.runDemo2();
        System.out.println("==========================");
        demo.runDemo3();
    }

    public void runDemo1() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();

        // 写json
        File jsonFile = new File("demo.json");
        JsonGenerator generator = factory.createGenerator(jsonFile, JsonEncoding.UTF8);
        generator.writeStartObject();
        generator.writeStringField("message", "Hello Jackson!");
        generator.writeEndObject();
        generator.close();

        // 读json文件
        JsonParser parser = factory.createParser(jsonFile);
        // JsonToken.START_OBJECT
        JsonToken token = parser.nextToken();
        // JsonToken.FIELD_NAME
        token = parser.nextToken();
        if (token == JsonToken.FIELD_NAME) {
            p(parser.getCurrentName());
            token = parser.nextToken();
            p(parser.getText());
        }
        parser.close();
    }

    public void runDemo2() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // mapper各个特性只能设置一次，如果需要设置多次相同属性，需要创建多个实例
        //~ SerializationFeature，定义序列化特性

        // 格式化输出
        User user = new User("belonk", 10, "read");
        p(mapper.writeValueAsString(user));
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        p(mapper.writeValueAsString(user));
        mapper.disable(SerializationFeature.INDENT_OUTPUT);

        // to allow serialization of "empty" POJOs (no properties to serialize)
        // (without this setting, an exception is thrown in those cases)
        Object emptyBean = new Object();
        try {
            p(mapper.writeValueAsString(emptyBean));
        } catch (Exception e) {
            p("Serializing empty bean exception : " + e.getMessage());
        }
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        p(mapper.writeValueAsString(emptyBean));
        mapper.enable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // to write java.util.Date, Calendar as number (timestamp):
        Date now = new Date();
        p(mapper.writeValueAsString(now));
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        p(mapper.writeValueAsString(now));

        //~ DeserializationFeature for changing how JSON is read as POJOs:

        String json = "{\"name\":\"belonk\",\"age\":10,\"topHover\":\"\",\"unknown\":\"value\"}";
        // to prevent exception when encountering unknown property:
        try {
            User failUser = mapper.readValue(json, User.class);
        } catch (Exception e) {
            p("Deserializing unknown field json exception : " + e.getMessage());
        }
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        User successUser = mapper.readValue(json, User.class);
        p(successUser.toString());

        // to allow coercion of JSON empty String ("") to null Object value:
        successUser = mapper.readValue(json, User.class);
        p("topHover : " + successUser.getTopHover());
        mapper = mapper.copy();
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        successUser = mapper.readValue(json, User.class);
        p("topHover : " + successUser.getTopHover());
    }

    public void runDemo3() {
        ObjectMapper mapper = new ObjectMapper();
        // Convert from List<Integer> to int[]
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        System.out.println(integers);
        int[] ints = mapper.convertValue(integers, int[].class);
        System.out.println(Arrays.toString(ints));

        // Convert a POJO into Map
        User user = new User("belonk", 30, null);
        System.out.println(user);
        Map map = mapper.convertValue(user, Map.class);
        System.out.println(map);

        // Convert a map into POJO
        user = mapper.convertValue(map, User.class);
        System.out.println(user);

        // decode Base64! (default byte[] representation is base64-encoded String)
        String base64 = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0aGlz";
        byte[] binary = mapper.convertValue(base64, byte[].class);
        System.out.println(Arrays.toString(binary));
        String str = mapper.convertValue(binary, String.class);
        System.out.println(str);

    }

    private void p(String string) {
        System.out.println(string);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Protected Methods
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Property accessors
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */



    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Inner classes
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public static class User {
        private String name;
        private Integer age;
        private String topHover;

        public User() {
        }

        public User(String name, Integer age, String topHover) {
            this.name = name;
            this.age = age;
            this.topHover = topHover;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getTopHover() {
            return topHover;
        }

        public void setTopHover(String topHover) {
            this.topHover = topHover;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", topHover='" + topHover + '\'' +
                    '}';
        }
    }
}