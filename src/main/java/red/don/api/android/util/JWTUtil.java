package red.don.api.android.util;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import red.don.api.android.entity.UserEntity;

public class JWTUtil {

  private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

  /**
   * Parse JWT token to user entity.
   *
   * @param token JWT token.
   * @return User entity or null.
   */
  public static UserEntity parse(String token) {
    try {
      Map<String, Object> body = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
      return new UserEntity((String) body.get("email"), (String) body.get("name"), null, (long) body.get("token"));
    } catch (Exception exp) {
      return null;
    }
  }

  /**
   * Generate JWT token by user entity.
   *
   * @param user User entity.
   * @return JWT token.
   */
  public static String generate(UserEntity user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("email", user.getEmail());
    claims.put("name", user.getName());
    claims.put("token", user.getToken());
    return Jwts.builder().setClaims(claims).signWith(key).compact();
  }

}
