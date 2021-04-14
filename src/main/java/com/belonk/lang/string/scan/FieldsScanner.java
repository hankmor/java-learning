package com.belonk.lang.string.scan;

import com.belonk.util.PrintHelper;

import java.math.BigDecimal;
import java.util.Scanner;

import static com.belonk.util.PrintHelper.println;

/**
 * <p>Created by sun on 2016/1/13.
 *
 * @author sun
 * @version 1.0
 * @since 2.2.3
 */
public class FieldsScanner {
    //~ Static fields/initializers =====================================================================================

    //~ Instance fields ================================================================================================
    private String name;
    private long id;
    private int age;
    private BigDecimal salePrice;
    private double marketPrice;

    //~ Methods ========================================================================================================

    public FieldsScanner(String param) {
        Scanner scanner = new Scanner(param);
        // 使用正则表达式定界符
        scanner.useDelimiter("\\s*,\\s*");
        if (scanner.hasNext())
            this.name = scanner.next();
        if (scanner.hasNextLong())
            this.id = scanner.nextLong();
        if (scanner.hasNextInt())
            this.age = scanner.nextInt();
        if (scanner.hasNextBigDecimal())
            this.salePrice = scanner.nextBigDecimal();
        if (scanner.hasNextDouble())
            this.marketPrice = scanner.nextDouble();
    }

    public static void main(String[] args) {
        String str = "sun, 1, 10, 100.0, 88.01234";
        FieldsScanner fieldsScanner = new FieldsScanner(str);
        PrintHelper.println(fieldsScanner);
        // 交换顺序
        str = "sun, " + Integer.toString(10) + ", " + Long.toString(1L)
                + ", " + new BigDecimal("88.01234").toString() + ", " + Double.toString(100.0d);
        fieldsScanner = new FieldsScanner(str);
        PrintHelper.println(fieldsScanner);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }

    @Override
    public String toString() {
        return "FieldsScanner{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", salePrice=" + salePrice +
                ", marketPrice=" + marketPrice +
                '}';
    }
}
/* Output ：
FieldsScanner{name='sun', id=1, age=10, salePrice=100.0, marketPrice=88.01234}
FieldsScanner{name='sun', id=10, age=1, salePrice=88.01234, marketPrice=100.0}
*/