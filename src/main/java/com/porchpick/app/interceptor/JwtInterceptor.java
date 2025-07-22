package com.porchpick.app.interceptor;

import com.porchpick.app.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /*
        * 🧠 What the browser does (when CORS is triggered):
                🔍 Preflight Request (when using custom headers like Authorization, or non-simple methods like PUT, DELETE):

                Sends an OPTIONS request.

                Asks:

                “Hey server, can I make a real request from http://localhost:3000 to your backend on port 8080? I'll be sending an Authorization header, is that okay?”

                This looks like:
                Copy
                Edit
                OPTIONS /yardsales HTTP/1.1
                Origin: http://localhost:3000
                Access-Control-Request-Method: GET
                Access-Control-Request-Headers: Authorization


                ✅ Server responds with:
                Copy
                Edit
                HTTP/1.1 200 OK
                Access-Control-Allow-Origin: http://localhost:3000
                Access-Control-Allow-Methods: GET, POST, ...
                Access-Control-Allow-Headers: Authorization

                * 🚀 Browser says “Cool!” and sends the real request:

                http
                Copy
                Edit
                GET /yardsales
                Authorization: Bearer <token>
                Now your interceptor runs real auth checks.
        * */

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return false;
        }

        String token = authHeader.substring(7);
        if (!jwtService.isTokenValid(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired JWT");
            return false;
        }

        request.setAttribute("userId", jwtService.extractUserId(token));
        return true;
    }
} 