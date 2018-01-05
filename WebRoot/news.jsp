<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html class="no-js">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="keywords" content="" />
    <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title></title>

    <!--360 browser -->
    <meta name="renderer" content="webkit">
    <meta name="author" content="wos">
    <!--Win8 or 10 -->
    <meta name="msapplication-TileImage" content="images/i/app.png">
    <meta name="msapplication-TileColor" content="#e1652f">

    <link rel="icon" type="image/png" href="images/i/favicon.png">
    <link rel="stylesheet" href="assets/css/amazeui.min.css">
    <link rel="stylesheet" href="css/public.css">

    <!--[if (gte IE 9)|!(IE)]><!-->
    <script src="assets/js/jquery.min.js"></script>
    <!--<![endif]-->
    <!--[if lte IE 8 ]>
    <script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
    <script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
    <script src="assets/js/amazeui.ie8polyfill.min.js"></script>
    <![endif]-->
    <script src="assets/js/amazeui.min.js"></script>
    <script src="js/public.js"></script>
</head>
<body>



<div class="am-g am-container">
    <div class="am-u-sm-12 am-u-md-12">
        <div class="newstitles">
            <h2>${news.title}</h2>
            <span>${news.source}</span>
            <span>时间：${news.releaseTime}</span>   <span>阅读：${news.joinNum}</span>
        </div>
        <a href="#"><img src="Temp-images/ad2.png" class="am-img-responsive" width="100%"/></a>

        <div class="contents">
            <p dir="ltr"><span style="color: rgb(88, 88, 88); font-family: arial, &#39;microsoft yahei&#39;, 宋体; line-height: 28px; text-align: justify; font-size: 16px; background-color: rgb(255, 255, 255);">
    ${news.content}
    </span></p>
        </div>
        <!--<div class="shang">
            <img src="images/shang.png" >
        </div>-->
        <!--data-ds-short-name="amazeui" 多说的用户名-->
        <!--<div data-am-widget="duoshuo" class="am-duoshuo am-duoshuo-default" data-ds-short-name="amazeui">
            <div class="ds-thread" >
            </div>
        </div>-->
        <ul class="am-comments-list am-comments-list-flip">
        <c:forEach items="${comments}" var="comment">
                                        <li class="am-comment">
                                            <!-- 评论容器 -->
                                            <a href="">
                                                <img class="am-comment-avatar" src="pic/user.jpg" />
                                                <!-- 评论者头像 -->
                                            </a>

                                            <div class="am-comment-main">
                                                <!-- 评论内容容器 -->
                                                <header class="am-comment-hd">
                                                    <!--<h3 class="am-comment-title">评论标题</h3>-->
                                                    <div class="am-comment-meta">
                                                        <!-- 评论元数据 -->
                                                        <a href="#" class="am-comment-author">${comment.userName}</a>
                                                        <!-- 评论者 -->
                                                        评论于
                                                        <time datetime="">${comment.createTime}</time>
                                                        <div style="float:right;">
                                                        <!-- 根据后台数据选择显示 -->
                                                        <span class="am-icon-md am-icon-frown-o" style="display:none;"></span>
                                                        <span class="am-icon-md am-icon-smile-o"></span>
                                                        </div>
                                                    </div>
                                                </header>

                                                <div class="am-comment-bd">
                                                    <div class="tb-rev-item " data-id="255776406962">
                                                        <div class="J_TbcRate_ReviewContent tb-tbcr-content ">
                                                            ${comment.comment}
                                                        </div>
                                                    </div>

                                                </div>
                                                <!-- 评论内容 -->
                                            </div>
                                        </li>
                                        </c:forEach>

                              <!--           <li class="am-comment">
                                            评论容器
                                            <a href="">
                                                <img class="am-comment-avatar" src="pic/user.jpg" />
                                                评论者头像
                                            </a>

                                            <div class="am-comment-main">
                                                评论内容容器
                                                <header class="am-comment-hd">
                                                    <h3 class="am-comment-title">评论标题</h3>
                                                    <div class="am-comment-meta">
                                                        评论元数据
                                                        <a href="#link-to-user" class="am-comment-author">b***1 (匿名)</a>
                                                        评论者
                                                        评论于
                                                        <time datetime="">2015年11月02日 17:46</time>
                                                    </div>
                                                </header>

                                                <div class="am-comment-bd">
                                                    <div class="tb-rev-item " data-id="255776406962">
                                                        <div class="J_TbcRate_ReviewContent tb-tbcr-content ">
                                                            摸起来丝滑柔软，不厚，没色差，颜色好看！买这个衣服还接到诈骗电话，我很好奇他们是怎么知道我买了这件衣服，并且还知道我的电话的！
                                                        </div>
                                                        <div class="tb-r-act-bar">
                                                            颜色分类：柠檬黄&nbsp;&nbsp;尺码：S
                                                        </div>
                                                    </div>

                                                </div>
                                                评论内容
                                            </div>
                                        </li> -->

                                    </ul>

    </div>

    </div>
</div>

</body>
</html>