import { i18n } from './utils';

const baseApiPath = '/api/public/io/github/dreammooncai/yukireflection/';

const navigationLinks = {
    start: [
        '/guide/home',
        '/guide/quick-start'
    ],
    config: [
        '/config/api-example',
        '/config/api-exception'
    ],
    apiDocs: [
        '/api/home',
        '/api/public/',
        '/api/features',
        '/api/special'
    ],
    publicApi: [
        baseApiPath + 'KYukiReflection',
        baseApiPath + 'type/android/KComponentTypeFactory',
        baseApiPath + 'type/android/KGraphicsTypeFactory',
        baseApiPath + 'type/android/KViewTypeFactory',
        baseApiPath + 'type/kotlin/KVariableTypeFactory',
        baseApiPath + 'type/defined/KDefinedTypeFactory',
        baseApiPath + 'log/KYLog',
        baseApiPath + 'factory/KFunctionAttachFactory',
        baseApiPath + 'factory/KJvmFactory',
        baseApiPath + 'factory/KotlinFactory',
        baseApiPath + 'factory/KReflectionFactory',
        baseApiPath + 'finder/callable/KFunctionFinder',
        baseApiPath + 'finder/callable/KConstructorFinder',
        baseApiPath + 'finder/callable/KPropertyFinder',
        baseApiPath + 'finder/classes/KClassFinder',
        baseApiPath + 'finder/classes/rules/result/KCallableRulesResult',
        baseApiPath + 'finder/classes/rules/KCallableRules',
        baseApiPath + 'finder/classes/rules/KPropertyRules',
        baseApiPath + 'finder/classes/rules/KFunctionRules',
        baseApiPath + 'finder/classes/rules/KConstructorRules',
        baseApiPath + 'finder/signature/support/KFunctionSignatureSupport',
        baseApiPath + 'finder/signature/support/KPropertySignatureSupport',
        baseApiPath + 'finder/signature/KFunctionSignatureFinder',
        baseApiPath + 'finder/signature/KPropertySignatureFinder',
        baseApiPath + 'finder/base/KBaseFinder',
        baseApiPath + 'finder/base/rules/KCountRules',
        baseApiPath + 'finder/base/rules/KModifierRules',
        baseApiPath + 'finder/base/rules/KNameRules',
        baseApiPath + 'finder/base/rules/KObjectRules',
        baseApiPath + 'core/KTypeBuild',
        baseApiPath + 'helper/KYukiHookHelper',
        baseApiPath + 'bean/KVariousClass',
        baseApiPath + 'bean/KCurrentClass',
        baseApiPath + 'bean/KGenericClass'
    ],
    about: [
        '/about/changelog',
        '/about/future',
        '/about/contacts',
        '/about/about'
    ]
};

export const configs = {
    dev: {
        dest: 'dist',
        port: 9000
    },
    website: {
        base: '/KYukiReflection/',
        icon: '/KYukiReflection/images/logo.png',
        logo: '/images/logo.png',
        title: 'KYuki Reflection',
        locales: {
            '/en/': {
                title: 'KYuki Reflection',
                lang: 'en-US',
                description: 'An efficient Reflection API for Java and Android built in Kotlin - Fully attach Kotlin high-order reflections.Fully attach Kotlin high-order reflections.'
            },
            '/zh-cn/': {
                title: 'KYuki Reflection',
                lang: 'zh-CN',
                description: '一个使用 Kotlin 构建的用于 Java 和 Android 平台高效反射 API - 完全附加 Kotlin 高阶反射'
            }
        }
    },
    github: {
        repo: 'https://github.com/DreamMoonCai/YukiReflection',
        branch: 'master',
        dir: 'docs-source/src'
    }
};

export const navBarItems = {
    '/en/': [{
        text: 'Navigation',
        children: [{
            text: 'Get Started',
            children: [
                { text: 'Introduce', link: i18n.string(navigationLinks.start[0], 'en') },
                { text: 'Quick Start', link: i18n.string(navigationLinks.start[1], 'en') }
            ]
        }, {
            text: 'Configs',
            children: [
                { text: 'API Basic Configs', link: i18n.string(navigationLinks.config[0], 'en') },
                { text: 'API Exception Handling', link: i18n.string(navigationLinks.config[1], 'en') }
            ]
        }, {
            text: 'API Document',
            children: [{ text: 'Document Introduction', link: i18n.string(navigationLinks.apiDocs[0], 'en') }, {
                text: 'Public API',
                link: i18n.string(navigationLinks.publicApi[0], 'en'),
                activeMatch: i18n.string(navigationLinks.apiDocs[1], 'en')
            }, {
                text: 'Features',
                link: i18n.string(navigationLinks.apiDocs[2], 'en')
            }, {
                text: 'Special',
                link: i18n.string(navigationLinks.apiDocs[3], 'en')
            }]
        }, {
            text: 'About',
            children: [
                { text: 'Changelog', link: i18n.string(navigationLinks.about[0], 'en') },
                { text: 'Looking for Future', link: i18n.string(navigationLinks.about[1], 'en') },
                { text: 'Contact Us', link: i18n.string(navigationLinks.about[2], 'en') },
                { text: 'About this Document', link: i18n.string(navigationLinks.about[3], 'en') }
            ]
        }]
    }, {
        text: 'Contact Us',
        link: i18n.string(navigationLinks.about[2], 'en')
    }],
    '/zh-cn/': [{
        text: '导航',
        children: [{
            text: '入门',
            children: [
                { text: '介绍', link: i18n.string(navigationLinks.start[0], 'zh-cn') },
                { text: '快速开始', link: i18n.string(navigationLinks.start[1], 'zh-cn') }
            ]
        }, {
            text: '配置',
            children: [
                { text: 'API 基本配置', link: i18n.string(navigationLinks.config[0], 'zh-cn') },
                { text: 'API 异常处理', link: i18n.string(navigationLinks.config[1], 'zh-cn') }
            ]
        }, {
            text: 'API 文档',
            children: [{ text: '文档介绍', link: i18n.string(navigationLinks.apiDocs[0], 'zh-cn') }, {
                text: 'Public API',
                link: i18n.string(navigationLinks.publicApi[0], 'zh-cn'),
                activeMatch: i18n.string(navigationLinks.apiDocs[1], 'zh-cn')
            }, {
                text: '功能介绍',
                link: i18n.string(navigationLinks.apiDocs[2], 'zh-cn')
            }, {
                text: '特色功能',
                link: i18n.string(navigationLinks.apiDocs[3], 'zh-cn')
            }]
        }, {
            text: '关于',
            children: [
                { text: '更新日志', link: i18n.string(navigationLinks.about[0], 'zh-cn') },
                { text: '展望未来', link: i18n.string(navigationLinks.about[1], 'zh-cn') },
                { text: '联系我们', link: i18n.string(navigationLinks.about[2], 'zh-cn') },
                { text: '关于此文档', link: i18n.string(navigationLinks.about[3], 'zh-cn') }
            ]
        }]
    }, {
        text: '联系我们',
        link: i18n.string(navigationLinks.about[2], 'zh-cn')
    }]
};

export const sideBarItems = {
    '/en/': [{
        text: 'Get Started',
        collapsible: true,
        children: i18n.array(navigationLinks.start, 'en')
    }, {
        text: 'Configs',
        collapsible: true,
        children: i18n.array(navigationLinks.config, 'en')
    }, {
        text: 'API Document',
        collapsible: true,
        children: [i18n.string(navigationLinks.apiDocs[0], 'en'), {
            text: 'Public API' + i18n.space,
            collapsible: true,
            children: i18n.array(navigationLinks.publicApi, 'en')
        }, i18n.string(navigationLinks.apiDocs[2], 'en'), i18n.string(navigationLinks.apiDocs[3], 'en')]
    }, {
        text: 'About',
        collapsible: true,
        children: i18n.array(navigationLinks.about, 'en')
    }],
    '/zh-cn/': [{
        text: '入门',
        collapsible: true,
        children: i18n.array(navigationLinks.start, 'zh-cn')
    }, {
        text: '配置',
        collapsible: true,
        children: i18n.array(navigationLinks.config, 'zh-cn')
    }, {
        text: 'API 文档',
        collapsible: true,
        children: [i18n.string(navigationLinks.apiDocs[0], 'zh-cn'), {
            text: 'Public API' + i18n.space,
            collapsible: true,
            children: i18n.array(navigationLinks.publicApi, 'zh-cn')
        }, i18n.string(navigationLinks.apiDocs[2], 'zh-cn'), i18n.string(navigationLinks.apiDocs[3], 'zh-cn')]
    }, {
        text: '关于',
        collapsible: true,
        children: i18n.array(navigationLinks.about, 'zh-cn')
    }],
};