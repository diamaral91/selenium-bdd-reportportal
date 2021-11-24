package br.com.frontendproject.pages;

import br.com.frontendproject.apis.frontendprojectServiceAPI;
import br.com.frontendproject.objects.AprovacaoObj;
import br.com.frontendproject.support.DriverWait;
import br.com.frontendproject.utils.JsonUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AprovacaoPage {

    private AprovacaoObj aprovacaoObj;
    private DriverWait wait;
    private FormalizacaoServiceAPI formalizacaoService;

    public AprovacaoPage(final WebDriver driver) {
        this.aprovacaoObj = new AprovacaoObj(driver);
        this.wait = new DriverWait(driver);
        this.formalizacaoService = new FormalizacaoServiceAPI();
        PageFactory.initElements(driver, this);
    }

    public String checkStatus(final String status) {
        this.wait.waitForClickableButton(this.aprovacaoObj.beneficioText).click();
        int count = 0;
        String text = "";
        while (count < 60) {
            final WebElement statusElement = this.wait.waitElementList(this.aprovacaoObj.statusText).get(0);
            text = this.wait.waitForVisibleElement(statusElement).getText();
            if (status.equalsIgnoreCase(text)) {
                return text;
            } else {
                count++;
                this.wait.setWait(2);
            }
        }
        return text;
    }

    public void formalizacaoCamunda(final String email, final String cpf, final int posicao) {
        final String frontend = new frontendprojectServiceAPI().getCustomersDocumentType(cpf, "origination.id");
        final String idDadosPessoais = this.getIdDadosPessoais(frontend, posicao);

        final String token = new FormalizacaoServiceAPI().gerarToken();
//        final String idAnaliseEspecifica = this.formalizacaoService.getObtemNovaTarefaDeAnalise(token, idDadosPessoais, frontend);

        final String body = this.setBody(email);
        this.formalizacaoService.postAlterarStatusTarefaDeAnalise(token, idDadosPessoais, body);
    }

    private String setBody(final String email) {
        final JsonUtils json = new JsonUtils("src/main/resources/collections/formalizacao.json");
        String body = "";
        if ("aprova".equals(email)) {
            body = json.getValue("alterarStatus.aprova");
        } else if ("pendenciamesa".equals(email)) {
            body = json.getValue("alterarStatus.pendenciamesa");
        }
        return body;
    }

    private String getIdDadosPessoais(final String frontend, final int posicao) {
        final List<String> tipoAnaliseList = this.formalizacaoService.getAnalises(frontend).getList("[" + posicao + "].analises.tipoAnalise");
        final List<String> idAnaliseList = this.formalizacaoService.getAnalises(frontend).getList("[" + posicao + "].analises.id");
        for (int count = 0; count < tipoAnaliseList.size(); count++) {
            if ("DADOS_PESSOAIS".equalsIgnoreCase(tipoAnaliseList.get(count))) {
                return String.valueOf(idAnaliseList.get(count));
            }
        }
        throw new RuntimeException("DADOS_PESSOAIS not_found");
    }

}
