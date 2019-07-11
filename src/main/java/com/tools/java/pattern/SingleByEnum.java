package com.tools.java.pattern;

/**
 * 使用枚举类型实现单例模式
 */

public enum SingleByEnum {
    INSTANCE;
    private Resource instance;

    SingleByEnum() {
        instance = new Resource();
    }

    public Resource getInstance() {
        return instance;
    }

    class Resource {
        private Resource() {
        }

        public void prit() {
            System.out.println("get resource instance");
        }
    }
}


class eme {

    public static void main(String[] args) {
        SingleByEnum.Resource resource = SingleByEnum.INSTANCE.getInstance();
        resource.prit();

    }

}