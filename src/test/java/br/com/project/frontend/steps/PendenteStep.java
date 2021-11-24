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

    @Dado("que frontend do cpf {} com produto {} seja pendenciada")
    public void que_frontend_seja_pendenciada(final String cpf, final String produto) {
        this.base.setCpf(cpf);
        this.base.setProduto(produto);

        if ("Receber".equals(produto)) {
            new MesaPages(this.base.getDriver()).efetuarLogin().iniciarTarefa().analisarTarefa(cpf);
            new AprovacaoPage(this.base.getDriver()).formalizacaoCamunda("pendenciamesa", this.base.getCpf(), 0);

        } else {
            new LoginPage(this.base.getDriver()).efetuarLogin();
            new PesquisarPorCpfPage(this.base.getDriver()).pesquisarCpf(cpf, produto);

            final AnaliseFraudeBancoPagadorPage analiseFraudeBancoPagador = new AnaliseFraudeBancoPagadorPage(this.base.getDriver());
            Assertions.assertEquals(cpf, analiseFraudeBancoPagador.checkCpfDadosPessoa());
            analiseFraudeBancoPagador.analisarfrontend(BackofficeEnum.PENDENCIAR);

            final PendenciarPage pendenciar = new PendenciarPage(this.base.getDriver());
            pendenciar.selecionarMotivoOuAtributoOpcional(BackofficeEnum.DOC_IDENT_NAO_ENV)
                    .selecionarMotivoOuAtributoOpcional(BackofficeEnum.DADOS_PESSOAIS)
                    .selecionarMotivoOuAtributoOpcional(BackofficeEnum.NOME_MAE)
                    .selecionarMotivoOuAtributoOpcional(BackofficeEnum.DATA_NASCIMENTO);
            pendenciar.pendenciarfrontend();
        }
    }

    @Quando("busco atendimento existente na sales emulator")
    public void busco_atendimento_existente_na_sales_emulator() {
        this.base.getDriver().get(getValue("urlBase"));
        new HomePage(this.base.getDriver()).buscarAtendimento(this.base.getCpf());
    }

    @Quando("identifico que existem pendencias para resolver")
    public void identifico_que_existem_pendencias_para_resolver() {
        final List<String> pendencias = new ArrayList<>(Arrays.asList("Reenviar documento de Identificação",
                "Preenchimento de Dados Complementares"));

        final PropostaPendentePage propostaPendente = new PropostaPendentePage(this.base.getDriver()).accessFrame();
        Assertions.assertEquals(pendencias.get(0), propostaPendente.checkPendencia().get(0).getText());
        Assertions.assertEquals(pendencias.get(1), propostaPendente.checkPendencia().get(1).getText());

        propostaPendente.resolverProblemas();
    }

    @Quando("preencho os dados pessoais corretamente")
    public void preencho_os_dados_pessoais_corretamente() {
        new DadosClientePage(this.base.getDriver()).preencherDadosPessoaisPendentes();
    }

    @Quando("confiro o resumo da contratação")
    public void confiro_os_produtos_contratados() {
        new ResumoContratacaoPage(this.base.getDriver()).continuar();
    }

    @Quando("escolho método de cadastro de documentos")
    public void escolho_metodo_de_cadastro_de_documentos() {
        new EnvioDocumentosPage(this.base.getDriver()).selecionarMensagemTexto(EnvioDocumentosEnum.MENSAGEM_TEXTO);
        this.base.setCodUrl(new DocumentosDistanciaPage(this.base.getDriver()).reenviarMensagem());
    }

    @Quando("realizo checklist do documento novamente")
    public void realizo_checklist_do_documento_novamente() {
        final CheckListPage checkList = new CheckListPage(this.base.getDriver());
        checkList.completarCheckList(this.base.getCpf(), this.base.getCodUrl());
        Assertions.assertTrue(checkList.checkEtapas());
    }

    @Mas("aprovo na mesa caso diferente de Cartão")
    public void se_produto_for_diferente_de_cartao_beneficio() {
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
    public void frontend_retorna_para_analise_de_fraude() {
        final String status = "Efetivado";

        final AprovacaoPage aprovacao = new AprovacaoPage(this.base.getDriver());
        Assertions.assertEquals(aprovacao.checkStatus(status), status);
    }

    @After(value = "@pendente", order = 2)
    public void closeUp(final Scenario scenario) {
        this.base.tearDown(scenario);
    }

}
