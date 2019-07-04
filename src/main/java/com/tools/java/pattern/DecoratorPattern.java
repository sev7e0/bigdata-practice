package com.tools.java.pattern;

/**
 * 装饰器模式：
 *
 * 目的：动态的给一个对象添加一些额外的操作，附加上更多额外的责任，而客户端在装饰前后感受不到差异
 *      装饰器在不添加更多子类的情况下，将对象的功能拓展。
 *
 * 角色：Component: 抽象构件
 *      ConcreteComponent: 具体构件
 *      Decorator: 抽象装饰类
 *      ConcreteDecorator: 具体装饰类
 *
 * 模式分析：
 *      与继承关系相比，关联关系的主要优势在于不会破坏类的封装性，而且继承是一种耦合度较大的静态关系，无法在程序运行时动态扩展。在软件开发阶段，关联关系虽然不会比继承关系减少编码量，但是到了软件维护阶段，由于关联关系使系统具有较好的松耦合性，因此使得系统更加容易维护。当然，关联关系的缺点是比继承关系要创建更多的对象。
 *      使用装饰模式来实现扩展比继承更加灵活，它以对客户透明的方式动态地给一个对象附加更多的责任。装饰模式可以在不需要创造更多子类的情况下，将对象的功能加以扩展。
 *
 * 优点：装饰模式与继承关系的目的都是要拓展对象的功能，但是装饰模式可以提供笔记城更多的灵活性
 *      可以通过动态的方式来拓展一个对象的功能，通过配置文件可以在运行时选择不同的装饰器，从而实现不同的行为
 *      通过使用不同的具体装饰类以及这些装饰类的排列组合，可以创造出不同的行为的组合，可以使用多个具体装饰累修饰一个对象，，得到一个功能更强大的对象
 *      具体构建类和具体装饰累可以独立变化，用户可以根据需要增加新的具体构件类和具体装饰类，在使用时再对其进行组合，原有代码无须改变，符合“开闭原则”
 * 缺点：每次新增一个装饰类都要创建一个对象，这样导致产生的小对象过多，增加了系统的复杂度，同时加大了学习和理解的难度。
 *      由于这种更加灵活的特性，导致在出错时排插问题更加困嫩需要开发人选逐层的排查。
 *
 * 适用环境：
 *      在不影响其他对象的情况下，以动态、透明的方式给单个对象添加职责。
 *      需要动态地给一个对象增加功能，这些功能也可以动态地被撤销。
 *      当不能采用继承的方式对系统进行扩充或者采用继承不利于系统扩展和维护时。不能采用继承的情况主要有两类：
 *          - 第一类是系统中存在大量独立的扩展，为支持每一种组合将产生大量的子类，使得子类数目呈爆炸性增长；
 *          - 第二类是因为类定义不能继承（如final类）.
 *
 * 注意事项：
 *      一个装饰类的接口必须要与被装饰类的接口保持相同，装饰细节对客户端不可见
 *      尽量保持具体构件类Component作为一个“轻”类，也就是说不要把太多的逻辑和状态放在具体构件类中，可以通过装饰类
 *
 * 装饰者模式的设计原则为：
 *      对扩展开放、对修改关闭，这句话体现在我如果想扩展被装饰者类的行为，无须修改装饰者抽象类，只需继承装饰者抽象类，实现额外的
 *      一些装饰或者叫行为即可对被装饰者进行包装。所以：扩展体现在继承、修改体现在子类中，而不是具体的抽象类，这充分体现了依赖倒置原则.
 */
public class DecoratorPattern {

    public static void main(String[] args) {
//        ProductionIPhone productionIPhone = new FuShiKang();

        ProductionIPhone decorator = new Decorator02(new Decorator01(new FuShiKang()));

        decorator.productionBattery();

        decorator.productionScreen();
    }
}

interface ProductionIPhone{

    void productionBattery();

    void productionScreen();

}

class FuShiKang implements ProductionIPhone{

    @Override
    public void productionBattery() {
        System.out.println("FuShiKang production the iphong battery");
    }

    @Override
    public void productionScreen() {
        System.out.println("FuShiKang production the iphong screen");
    }
}

/**
 * Decorator抽象类中，持有Human接口，方法全部委托给该接口调用，目的是交给该接口的实现类即子类进行调用。
 */
abstract class FuShiKangDecorator implements ProductionIPhone{

    private ProductionIPhone productionIPhone;

    public FuShiKangDecorator(ProductionIPhone productionIPhone) {
        this.productionIPhone = productionIPhone;
    }

    @Override
    public void productionBattery() {
        productionIPhone.productionBattery();
    }

    @Override
    public void productionScreen() {
        productionIPhone.productionScreen();
    }
}

class Decorator01 extends FuShiKangDecorator{

    public Decorator01(ProductionIPhone productionIPhone) {
        super(productionIPhone);
    }

    public void productionCharger(){
        System.out.println("FuShiKang production the iphong Charger too");
    }


    @Override
    public void productionBattery() {
        /**
         * Decorator抽象类的子类（具体装饰者），里面都有一个构造方法调用super(human),这一句就体现了抽象类依赖于子类实现即抽象依赖于实现的原则
         */
        super.productionBattery();
        productionCharger();
    }

    @Override
    public void productionScreen() {
        super.productionScreen();
    }
}

class Decorator02 extends FuShiKangDecorator{

    public Decorator02(ProductionIPhone productionIPhone) {
        super(productionIPhone);
    }

    public void assembleIPhone(){
        System.out.println("Started assemble iphone");
    }


    @Override
    public void productionBattery() {
        super.productionBattery();
    }

    @Override
    public void productionScreen() {
        super.productionScreen();
        assembleIPhone();
    }
}






























