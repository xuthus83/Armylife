package cn.armylife.zuul.config;

import cn.armylife.zuul.feign.MemberService;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class LoginFilter  extends ZuulFilter {

    private static Logger logger = Logger.getLogger("LoginFilter.class");
    @Autowired
    MemberService memberService;

    //非拦截地址
    private List<String> paths;
    public LoginFilter() {
        super();
        paths = new ArrayList<>();
        paths.add("/Member/loginUser");
        paths.add("/Member/register");
        paths.add("/Member/reloadSession");
        paths.add("/**/swagger**/**");
        paths.add("/**/v2/api-docs");
        paths.add("/**/*.css");
        paths.add("/**/*.jpg");
        paths.add("/**/*.png");
        paths.add("/**/*.gif");
        paths.add("/**/*.js");
        paths.add("/**/*.svg");
    }


    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String uri=request.getRequestURI();
        logger.info("uri:{}"+ uri);
        PathMatcher matcher = new AntPathMatcher();
        Optional<String> optional =paths.stream().filter(t->matcher.match(t,uri)).findFirst();
        return !optional.isPresent();
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        logger.info("send  {} request to {} "+request.getMethod()+request.getRequestURL().toString());

        return null;

    }
}