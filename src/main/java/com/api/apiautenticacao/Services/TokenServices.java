package com.api.apiautenticacao.Services;

import com.api.apiautenticacao.Model.TokensModel;
import com.api.apiautenticacao.Model.UserModel;
import com.api.apiautenticacao.repository.TokensRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

@Service
public class TokenServices {
    private TokensRepository repo;

    //Chave secreta para assinatura do jwt, gerada usando a biblioteca jjwt
    private final Key jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //Tempo de expiração do jwt de 15 minutos
    private final long jwtExpirationMs = 900000;
    //tempo de expiração do jwt refresh de 7 dias
    private final long jwtRefreshMs = 604800000;

    public TokenServices() {

    }

    //Método do Token Acess
    public String generateJwtToken(UserModel user) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtExpirationMs);
        //Aqui você pode usar a biblioteca jjwt para criar um token JWT
        //com as informações do usuário e a chave secreta
        return Jwts.builder()
                .setSubject(user.getEmail()) //Informação principal do token (também pode ser ID)
                .claim("role", user.getRole().getName()) //Adiciona a role do usuário
                .setIssuedAt(now) //Data de emissão do token
                .setExpiration(expiration) //Data de expiração do token
                .signWith(jwtSecretKey) //Assina o token com a chave secreta, para nn haver falsificação
                .compact();

    }

    //Método do Token Refresh
    public TokensModel generateToeknRefresh(UserModel user) {
        TokensModel tk = new TokensModel();

        tk.setUser(user);
        tk.setToken(UUID.randomUUID().toString()); //Gera um token aleatório para refresh

        //Seta a data de expiração e data de criação do token
        tk.setExpires_at(new Timestamp(currentTimeMillis() + jwtRefreshMs));
        tk.setCreated_at(new Timestamp(System.currentTimeMillis()));
        tk.setIs_revoked(false);

        return repo.save(tk);
    }

    //Método para verificar se o token expirou ou não
    public TokensModel verifiedTokenExpiration(TokensModel tk ) {
        //Se a data de expira tiver antes da data atual, quer dizer que expirou
        if (tk.getExpires_at().before(new Timestamp(System.currentTimeMillis()))) {
            //Se sim apaga do banco
            repo.delete(tk);
            throw new RuntimeException("Token expirado, faça login novamente!");
        }
        return tk;
    }
}
