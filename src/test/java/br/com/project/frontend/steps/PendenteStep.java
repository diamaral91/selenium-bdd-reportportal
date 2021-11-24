package br.com.frontendproject.steps;

import br.com.frontendproject.enums.EnvioDocumentosEnum;
import br.com.frontendproject.pages.AprovacaoPage;
import br.com.frontendproject.pages.CheckListPage;
import br.com.frontendproject.pages.DadosClientePage;
import br.com.frontendproject.pages.DocumentosDistanciaPage;
import br.com.frontendproject.pages.EnvioDocumentosPage;
import br.com.frontendproject.pages.HomePage;
import br.com.frontendproject.pages.PropostaPendentePage;
import br.com.frontendproject.pages.ResumoContratacaoPage;
import br.com.frontendproject.pages.backofice.AnaliseFraudeBancoPagadorPage;
import br.com.frontendproject.pages.backofice.LoginPage;
import br.com.frontendproject.pages.backofice.PendenciarPage;
import br.com.frontendproject.pages.backofice.PesquisarPorCpfPage;
import br.com.frontendproject.pages.mesa.MesaPages;
import br.com.frontendproject.support.TestBase;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Entao;
import io.cucumber.java.pt.Mas;
import io.cucumber.java.pt.Quando;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.frontendproject.utils.PropertieUtils.getValue;

public class PendenteStep {

    private TestBase base;

    @Before(value = "@pendente", order = 2)
    public void init() {
        this.base = new TestBase();
    }

    @Dado("que ")
    public void que_frontend_a(final String cpf, final String produto) {
        this.base.setCpf(cpf);
        this.base.setProduto(produto);

        if ("Receber".equals(produto)) {
            new MesaPages(this.base.getDriver()).efetuarLogin().iniciarTarefa().analisarTarefa(cpf);
            new AprovacaoPage(this.base.getDriver()).formalizacaoCamunda("pendenciamesa", this.base.getCpf(), 0);

        }
    }

    @Quando("busco ")
    public void busco_atendimento_() {
        this.base.getDriver().get(getValue("urlBase"));
        new HomePage(this.base.getDriver()).buscarAtendimento(this.base.getCpf());
    }

    @Quando("identifico ")
    public void identifico_() {
        final List<String> pendencias = new ArrayList<>(Arrays.asList("Reenviar documento de Identificação",
                "Preenchimento de Dados Complementares"));

        final PropostaPendentePage propostaPendente = new PropostaPendentePage(this.base.getDriver()).accessFrame();
        Assertions.assertEquals(pendencias.get(0), propostaPendente.checkPendencia().get(0).getText());
        Assertions.assertEquals(pendencias.get(1), propostaPendente.checkPendencia().get(1).getText());

        propostaPendente.resolverProblemas();
    }

    @Quando("preencho ")
    public void preencho_os_() {
        new DadosClientePage(this.base.getDriver()).preencherDadosPessoaisPendentes();
    }

    @Quando("confiro")
    public void confiro_os_() {
        new ResumoContratacaoPage(this.base.getDriver()).continuar();
    }

    @Quando("escolho")
    public void escolho_() {
        new EnvioDocumentosPage(this.base.getDriver()).selecionarMensagemTexto(EnvioDocumentosEnum.MENSAGEM_TEXTO);
        this.base.setCodUrl(new DocumentosDistanciaPage(this.base.getDriver()).reenviarMensagem());
    }

    @Quando("realizo")
    public void realizo_() {
        final CheckListPage checkList = new CheckListPage(this.base.getDriver());
        checkList.completarCheckList(this.base.getCpf(), this.base.getCodUrl());
        Assertions.assertTrue(checkList.checkEtapas());
    }

    @Mas("aprovo")
    public void se_produto_() {
        if ("Cartão".equalsIgnoreCase(this.base.getProduto())) {
            new LoginPage(this.base.getDriver()).efetuarLogin();
            new PesquisarPorCpfPage(this.base.getDriver()).pesquisarCpf(this.base.getCpf(), this.base.getProduto());

            final AnaliseFraudeBancoPagadorPage analiseFraudeBancoPagador = new AnaliseFraudeBancoPagadorPage(this.base.getDriver());
            Assertions.assertEquals(this.base.getCpf(), analiseFraudeBancoPagador.checkCpfDadosPessoa());
            analiseFraudeBancoPagador.analisarfrontend(BackofficeEnum.APROVAR).continuar();
        } else {
            new MesaPages(this.base.getDriver()).iniciarTarefa().analisarTarefa(this.base.getCpf());
            new AprovacaoPage(this.base.getDriver()).formalizacaoCamunda("aprova", this.base.getCpf(), 1);
        }
    }

    @Entao("frontend pendenciada retorna status efetivado")
    public void frontend_retorna() {
        final String status = "Efetivado";

        final AprovacaoPage aprovacao = new AprovacaoPage(this.base.getDriver());
        Assertions.assertEquals(aprovacao.checkStatus(status), status);
    }

    @After(value = "@pendente", order = 2)
    public void closeUp(final Scenario scenario) {
        this.base.tearDown(scenario);
    }

}
