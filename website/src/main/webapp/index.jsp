<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>

<html>
    <head>
        <title>Javacloud</title>
        <jsp:include page="jsp/include/htmlhead.jsp"/>
    </head>

    <body>
       
        <jsp:include page="jsp/include/header.jsp"/>

        <!--
        <div class="tm-home-img-container">
            <img src="img/tm-home-img.jpg" alt="Image" class="hidden-lg-up img-fluid">
        </div>
        -->

        <section class="tm-section">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 text-xs-center">
                        <h2 class="tm-gold-text tm-title">Welcome to Java Cloud</h2>
                        <p class="tm-subtitle">Welcome to Australias premier java hosting solution.<br/>
                        Java Cloud allows you to host your web application with simplicity.</p>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 col-xl-3">

                        <div class="tm-content-box">
                            <!--
                            <img src="img/tm-img-310x180-1.jpg" alt="Image" class="tm-margin-b-20 img-fluid">
                            -->
                            <h4 class="tm-margin-b-20 tm-gold-text">Business Hosting</h4>
                            <p class="tm-margin-b-20">Host your business applications with us,
                            either exclusively, or as part of your load balanced solution. We focus on
                            Small to Medium business, and will work with you to find the right solution
                            for you.</p>
                            <a href="#" class="tm-btn text-uppercase">Read more</a>
                        </div>

                    </div>

                    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 col-xl-3">

                        <div class="tm-content-box">
                            <!--
                            <img src="img/tm-img-310x180-2.jpg" alt="Image" class="tm-margin-b-20 img-fluid">
                            -->
                            <h4 class="tm-margin-b-20 tm-gold-text">Software Developers</h4>
                            <p class="tm-margin-b-20">Your job is to build awesome applications. Let
                            us take care of hosting them for you. We provide databases, application containers,
                            and monitoring, so you can focus on development.</p>
                            <a href="#" class="tm-btn text-uppercase">Read More</a>    
                        </div>  

                    </div>

                    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 col-xl-3">

                        <div class="tm-content-box">
                            <!--
                            <img src="img/tm-img-310x180-3.jpg" alt="Image" class="tm-margin-b-20 img-fluid">
                            -->
                            <h4 class="tm-margin-b-20 tm-gold-text">Educators and Students</h4>
                            <p class="tm-margin-b-20">Run Java training courses and help your students
                            host their solutions in the java cloud. <br/>
                            Or as a student learning web development programming, host your creation
                            within our java cloud.</p>
                            <a href="#" class="tm-btn text-uppercase">Read More</a>
                        </div>  

                    </div>

                    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-3 col-xl-3">

                        <div class="tm-content-box">
                            <!--
                            <img src="img/tm-img-310x180-4.jpg" alt="Image" class="tm-margin-b-20 img-fluid">
                            -->
                            <h4 class="tm-margin-b-20 tm-gold-text">Services</h4>
                            <p class="tm-margin-b-20">Interested in us developing a website solution
                            for you. We can host wordpress, integration with payments, build an ecommerce
                            solution, or help you fix your java applications. Contact us for a quote</p>
                            <a href="#" class="tm-btn text-uppercase">Read More</a>    
                        </div>  

                    </div>
                </div> <!-- row -->

                <div class="row tm-margin-t-big">
                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-6 col-xl-6">
                        <div class="tm-2-col-left">
                            
                            <h3 class="tm-gold-text tm-title">How does it work</h3>
                            <!--
                            <p class="tm-margin-b-30">Some text here</p>
                            <img src="img/tm-img-660x330-1.jpg" alt="Image" class="tm-margin-b-40 img-fluid">
                            -->
                            <p>Register and login to get your own linux container. This container will be available
                            through the internet at http://&lt;username&gt;.javacloud.com.au. Inside your linux container
                            choose your application server, e.g. jetty, tomcat, jboss, wildfly or glassfish. Then deploy your
                            web applications (war files) into your containers, using our admin portal. You application
                            will appear at http://&lt;username&gt;.javacloud.com.au/&lt;yourapp&gt;</p>
                            <p>It&apos;s just that simple! You get root access to your linux container.
                            Our default container is pretty small, but if you need more RAM, Disk Space,
                            a Database, or anything else, we can help you to scale to your needs. </p>
                            <p class="m-b-2">Yes we host non java things also, like php, wordpress, prestashop, etc</p>
                            <p class="m-b-2">Try it out today, or contact us for more information. </p>
                            <a href="login.jsp" class="tm-btn text-uppercase">Sign up</a>
                            <a href="contact.jsp" class="tm-btn text-uppercase">Contact Us</a>
                        </div>
                    </div>

                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-6 col-xl-6">

                        <div class="tm-2-col-right">

                            <div class="tm-2-rows-md-swap">
                                <div class="tm-overflow-auto row tm-2-rows-md-down-2">
                                    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6">
                                        <h3 class="tm-gold-text tm-title">
                                            Categories
                                        </h3>
                                        <nav>
                                            <ul class="nav">
                                                <li><a href="#" class="tm-text-link">Java development</a></li>
                                                <li><a href="#" class="tm-text-link">Hosting</a></li>
                                                <li><a href="#" class="tm-text-link">Technology news</a></li>
                                                <li><a href="#" class="tm-text-link">Business news</a></li>
                                                <li><a href="#" class="tm-text-link">Other</a></li>
                                            </ul>
                                        </nav>    
                                    </div> <!-- col -->

                                    <div class="col-xs-12 col-sm-6 col-md-6 col-lg-6 col-xl-6 tm-xs-m-t">
                                        <h3 class="tm-gold-text tm-title">
                                            Useful Links
                                        </h3>
                                        <nav>
                                            <ul class="nav">
                                                <li><a href="http://www.java.com" class="tm-text-link">Oracle Java</a></li>
                                                <li><a href="http://www.se-radio.net" class="tm-text-link">SE Radio</a></li>
                                                <li><a href="http://www.mkyong.com/" class="tm-text-link">Mykong</a></li>
                                                <li><a href="http://build-podcast.com" class="tm-text-link">Build Podcast</a></li>
                                                <li><a href="http://www.theserverside.com" class="tm-text-link">The Server Side</a></li>
                                            </ul>
                                        </nav>    
                                    </div> <!-- col -->
                                </div>                        
                                
                                <div class="row tm-2-rows-md-down-1 tm-margin-t-mid">
                                    <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12 col-xl-12">
                                        <h3 class="tm-gold-text tm-title tm-margin-b-30">Related Posts</h3>
                                        <div class="media tm-related-post">
                                          <div class="media-left media-middle">
                                            <a href="#">
                                              <img class="media-object" src="img/tm-img-240x120-1.jpg" alt="Generic placeholder image">
                                            </a>
                                          </div>
                                          <div class="media-body">
                                            <a href="#"><h4 class="media-heading tm-gold-text tm-margin-b-15">Lorem ipsum dolor</h4></a>
                                            <p class="tm-small-font tm-media-description">Aenean cursus tellus mauris, quis consequat mauris dapibus id. Donec scelerisque porttitor pharetra.</p>
                                          </div>
                                        </div>
                                        <div class="media tm-related-post">
                                          <div class="media-left media-middle">
                                            <a href="#">
                                              <img class="media-object" src="img/tm-img-240x120-2.jpg" alt="Generic placeholder image">
                                            </a>
                                          </div>
                                          <div class="media-body">
                                            <a href="#"><h4 class="media-heading tm-gold-text tm-margin-b-15">Lorem ipsum dolor</h4></a>
                                            <p class="tm-small-font tm-media-description">Aenean cursus tellus mauris, quis consequat mauris dapibus id. Donec scelerisque porttitor pharetra.</p>
                                          </div>
                                        </div>
                                        <div class="media tm-related-post">
                                          <div class="media-left media-middle">
                                            <a href="#">
                                              <img class="media-object" src="img/tm-img-240x120-3.jpg" alt="Generic placeholder image">
                                            </a>
                                          </div>
                                          <div class="media-body">
                                            <a href="#"><h4 class="media-heading tm-gold-text tm-margin-b-15">Lorem ipsum dolor</h4></a>
                                            <p class="tm-small-font tm-media-description">Aenean cursus tellus mauris, quis consequat mauris dapibus id. Donec scelerisque porttitor pharetra.</p>
                                          </div>
                                        </div>
                                    </div>
                                </div>    
                            </div>

                        </div>
                        
                    </div>
                </div> <!-- row -->

            </div>
        </section>
        
        <jsp:include page="jsp/include/footer.jsp"/>
    </body>
</html>
