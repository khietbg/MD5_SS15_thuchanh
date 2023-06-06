package rikkei.academy.security.jwt;


import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import rikkei.academy.security.userPrincipal.UserPrinciple;

import java.util.Date;

@Component
public class JwtProvider {
    // tao logger bat truong hop ngoai le
    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);
    //tao chuoi bi mat
    private String jwtSecret ="Minhkhiet1";
    //tao thoi gian chet
    private int jwtExpiration = 86400;
    //HÀM TIẾN HÀNH MÃ HÓA USER THÀNH CHUỖI TOKEN -> SẼ ĐƯỢC GỌI TẠI API LOGIN TRÊN CONTROLLER
    public String generateJwtToken(Authentication authentication){
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrinciple.getUserName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+ jwtExpiration*1000))
                .signWith(SignatureAlgorithm.HS256,jwtSecret)
                .compact();
    }
    //HÀM TIẾN HAH KIỂM TRA TÍNH HỢP LỆ CỦA TOKEN ĐANG ĐĂNG NHẬP
    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parse(authToken);
            return true;
        }catch (SignatureException e){
            logger.error("Invalid JWT signature -> Message:{} ",e);
        }catch (MalformedJwtException e){
            logger.error("Invalid JWT token -> Message:{} ",e);
        }catch (ExpiredJwtException e){
            logger.error("expired JWT token -> Message:{} ",e);
        }catch (UnsupportedJwtException e){
            logger.error("UnSupported JWT token -> Message:{} ",e);
        }catch (IllegalArgumentException e){
            logger.error("JWT claims string is empty-> Message:{} ",e);
        }
        return false;
    }
    // lay lai thong tin nguoi dung tu chinh token tao ra
    public String getUserNameFromJwtToken(String token){
        String username = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody().getSubject();
        return username;
    }

}
