#include "HelloWorldScene.h"

USING_NS_CC;

Scene* HelloWorld::createScene()
{
    // 'scene' is an autorelease object
    auto scene = Scene::create();
    
    // 'layer' is an autorelease object
    auto layer = HelloWorld::create();

    // add layer as a child to scene
    scene->addChild(layer);

    // return the scene
    return scene;
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{
    //////////////////////////////
    // 1. super init first
    if ( !Layer::init() )
    {
        return false;
    }
    
    Size visibleSize = Director::getInstance()->getVisibleSize();
    Vec2 origin = Director::getInstance()->getVisibleOrigin();

    /////////////////////////////
    // 2. add a menu item with "X" image, which is clicked to quit the program
    //    you may modify it.

    // add a "close" icon to exit the progress. it's an autorelease object
    auto closeItem = MenuItemImage::create(
                                           "CloseNormal.png",
                                           "CloseSelected.png",
                                           CC_CALLBACK_1(HelloWorld::menuCloseCallback, this));
    
	closeItem->setPosition(Vec2(origin.x + visibleSize.width - closeItem->getContentSize().width/2 ,
                                origin.y + closeItem->getContentSize().height/2));

    // create menu, it's an autorelease object
    auto menu = Menu::create(closeItem, NULL);
    menu->setPosition(Vec2::ZERO);
    this->addChild(menu, 1);

    /////////////////////////////
    // 3. add your codes below...

    // add a label shows "Hello World"
    // create and initialize a label
    
    auto title = Label::createWithTTF("Anchuo Fighter 2016", "fonts/Marker Felt.ttf", 24);
    
    // position the label on the center of the screen
    title->setPosition(Vec2(origin.x + visibleSize.width/2,
                            origin.y + visibleSize.height - title->getContentSize().height));

    // add the label as a child to this layer
    this->addChild(title, 1);
    
    auto addr = Label::createWithTTF("Server", "fonts/Marker Felt.ttf", 16);
    //addr->setTextColor(Color4B(0, 0, 0, 255));
    addr->setPosition(Vec2(origin.x + visibleSize.width/4, origin.y + visibleSize.height/3 - addr->getContentSize().height));
    this->addChild(addr, 1);
    
    auto addrstr = TextFieldTTF::createWithTTF("", "fonts/Marker Felt.ttf", 16);
    addrstr->setPosition(Vec2(origin.x + visibleSize.width/4 + addr->getContentSize().width, origin.y + visibleSize.height/3 - addr->getContentSize().height));
    this->addChild(addrstr, 1);
    
    auto nickname = Label::createWithTTF("Name", "fonts/Marker Felt.ttf", 16);
    nickname->setPosition(Vec2(origin.x + visibleSize.width/4, origin.y + visibleSize.height/3 - nickname->getContentSize().height * 2));
    this->addChild(nickname, 1);
    

    // add "HelloWorld" splash screen"
    auto sprite = Sprite::create("Logo.jpg");

    // position the sprite on the center of the screen
    sprite->setPosition(Vec2(visibleSize.width/2 + origin.x, visibleSize.height/2 + origin.y));

    // add the sprite as a child to this layer
    this->addChild(sprite, 0);
    
    return true;
}


void HelloWorld::menuCloseCallback(Ref* pSender)
{
    Director::getInstance()->end();

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
    exit(0);
#endif
}
