package it.regioneveneto.myp3.gestgraduatorie.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.collect.Lists.newArrayList;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("it.regioneveneto.myp3.gestgraduatorie"))
                .paths(PathSelectors.ant("/**"))
                .build()
                .apiInfo(apiInfo())
                /*.tags(new Tag("tag_api_gestore", "Servizi REST per la gestione dei gestore degli impianti"),
                        new Tag("tag_api_impianto", "Servizi REST per la gestione degli impianti"),
                        new Tag("tag_api_tipo_ostacolo", "Servizi REST per la gestione dei tipi ostacolo")
                        )*/
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET,
                        newArrayList(new ResponseMessageBuilder()
                                        .code(500)
                                        .message("Errore interno")
                                        .responseModel(new ModelRef("string"))
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(403)
                                        .message("Operazione non autorizzata")
                                        .build(),
                                new ResponseMessageBuilder()
                                        .code(404)
                                        .message("Operazione non prevista")
                                        .build())
                );

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Gestione Graduatorie REST API").description("API per la gestione dele graduatorie").version("0.0.1").build();
    }

}
