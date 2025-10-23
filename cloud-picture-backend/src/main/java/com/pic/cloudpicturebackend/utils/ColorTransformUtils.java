package com.pic.cloudpicturebackend.utils;


/**
 * 颜色转换工具类
 */
public class ColorTransformUtils {

    private ColorTransformUtils() {
        // 工具类不需要实例化
    }

    /**
     * 获取标准颜色（将数据万象的 5 位色值转为 6 位）
     *
     * @param color
     * @return
     */
    public static String getStandardColor(String color) {
        if (color.matches("0x[0-9a-fA-F]{6}]")) {
            return color;
        }
        // 去除 0x 前缀
        String subColor = color.substring(2);
        if (subColor.length() == 3) {
            return "0x000000";
        }
        int index = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++){
            char current = subColor.charAt(index);
            if (current == '0') {
                sb.append("00");
                index++;
            } else {
                if (index + 1 < subColor.length()) {
                    sb.append(current).append(subColor.charAt(index + 1));
                    index += 2;
                } else {
                    sb.append(current).append("0");
                    index++;
                }
            }
        }
        return "0x" + sb;
    }

    public static void main(String[] args) {
        // 测试
        System.out.println(getStandardColor("0x000"));  // 0x000000
        System.out.println(getStandardColor("0x0a00")); // 0x00a000
        System.out.println(getStandardColor("0xa0b40"));// 0xa0b400
        System.out.println(getStandardColor("0x0ab0")); // 0x00ab00
        System.out.println(getStandardColor("0x00ab")); // 0x0000ab
        System.out.println(getStandardColor("0x0ab00"));// 0x00ab00
    }
}
