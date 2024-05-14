package br.com.emannuelmorais.livrosws.config;


import br.com.emannuelmorais.livrosws.exception.ApiError;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    private final Environment environment;
    @Bean
    public GroupedOpenApi groupLivros() {
        return GroupedOpenApi.builder()
                .group("livros")
                .pathsToMatch("/livros/**")
                .addOpenApiCustomiser(customerGlobalHeaderOpenApiCustomiser())
                .build();
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(apiInfo())
                .components(apiComponents());
    }


    @Bean
    public OpenApiCustomiser customerGlobalHeaderOpenApiCustomiser() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            ApiResponses apiResponses = operation.getResponses();

            Content apiErrorContent = new Content()
                    .addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, new MediaType()
                            .schema(
                                    ModelConverters.getInstance().resolveAsResolvedSchema(
                                            new AnnotatedType(ApiError.class)).schema));

            apiResponses.addApiResponse("400", new ApiResponse()
                    .description("Requisição inválida")
                    .content(apiErrorContent));

            apiResponses.addApiResponse("401", new ApiResponse()
                    .description("Acesso não autorizado")
                    .content(apiErrorContent));

            apiResponses.addApiResponse("403", new ApiResponse()
                    .description("Acesso proibido")
                    .content(apiErrorContent));

            apiResponses.addApiResponse("404", new ApiResponse()
                    .description("Não encontrado")
                    .content(apiErrorContent));

            apiResponses.addApiResponse("422", new ApiResponse()
                    .description("Erro de negócio")
                    .content(apiErrorContent));

            apiResponses.addApiResponse("500", new ApiResponse()
                    .description("Erro no servidor")
                    .content(apiErrorContent));
        }));
    }

    private Info apiInfo() {
        String description = "Projeto LivrosWS - API para gerenciamento e vendas de livros. \n"
                + "\n";

        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            description += "Perfil: " + Arrays.toString(environment.getActiveProfiles()) + "\n\n";
        }

        description += "**Corpo da resposta no caso de erro (400, 401, 403, 404, 422, 500):**\n"
                + "\n"
                + "```json\n"
                + "{\n"
                + "  \"status\": 400,\n"
                + "  \"error\": \"string\",\n"
                + "  \"timestamp\": \"2023-12-31T23:59:59\",\n"
                + "  \"message\": \"string\",\n"
                + "  \"debugMessage\": \"string\",\n"
                + "  \"subErrors\": [\n"
                + "    {\n"
                + "      \"object\": \"string\",\n"
                + "      \"field\": \"string\",\n"
                + "      \"rejectedValue\": \"object\",\n"
                + "      \"message\": \"string\"\n"
                + "    }\n"
                + "  ]\n"
                + "}\n"
                + "```";

        return new Info()
                .title("LivrosWS")
                .description(description)
                .version("1.0.0");
    }


    private Components apiComponents() {
        return new Components()
                .addSecuritySchemes("chaveAcesso", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("chaveAcesso")
                        .description("A chave de acesso dos _endpoints_ da API."));
    }

}
