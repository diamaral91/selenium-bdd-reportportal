package br.com.frontendproject.steps;

import br.com.frontendproject.enums.BeneficiosEnum;
import br.com.frontendproject.enums.EnvioDocumentosEnum;
import br.com.frontendproject.pages.AprovacaoPage;
import br.com.frontendproject.pages.CheckListPage;
import br.com.frontendproject.pages.ConfiguracaoProdutoPage;
import br.com.frontendproject.pages.DadosClientePage;
import br.com.frontendproject.pages.DataPrevPage;
import br.com.frontendproject.pages.DocumentosDistanciaPage;
import br.com.frontendproject.pages.EnvioDocumentosPage;
import br.com.frontendproject.pages.HomePage;
import br.com.frontendproject.pages.OfertasPage;
import br.com.frontendproject.pages.ResumoContratacaoPage;
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

import static br.com.frontendproject.utils.PropertieUtils.getValue;

public class frontendStep {

    private TestBase base;

    @Before(value = "@frontend", order = 1)
    public void init() {
        this.base = new TestBase();
    }

    @Dado("que ")
    public void que_atendimento_tenha_sido_criado(final String cpf) {
        this.base.setCpf(cpf);
        new HomePage(this.base.getDriver()).criarAtendimento(cpf);
    }

    @Mas("avanço ")
    public void avanco_dataPrev_sem_consulta(){
        new DataPrevPage(this.base.getDriver()).continuarSemConsulta();
    }

    @Quando("aceito ")
    public void aceito_oferta_de_produto(final String produto) {
        this.base.setProduto(produto);
        final OfertasPage ofertas = new OfertasPage(this.base.getDriver());
        if ("Conta Corrente".equalsIgnoreCase(produto)) {
            final BeneficiosEnum beneficio = BeneficiosEnum.ANTECIPACAO;
            ofertas.associarBeneficio(beneficio.getValue());
        } else {
            ofertas.associarBeneficio(produto);
        }
    }

    @Quando("configuro ")
    public void configuro_IDConta_e_IDCartao(final String idconta, final String idCartao) {
        final ConfiguracaoProdutoPage configuracaoProduto = new ConfiguracaoProdutoPage(this.base.getDriver());
        if ("Cartão".equalsIgnoreCase(this.base.getProduto())) {
            configuracaoProduto.vincularCartaoCliente(idconta, idCartao);
        } else if ("Conta Corrente".equalsIgnoreCase(this.base.getProduto()) || "Receber".equalsIgnoreCase(this.base.getProduto())) {
            configuracaoProduto.salvarContinuar();
            configuracaoProduto.vincularCartaoCliente(idconta, idCartao);
        }
    }

    @Quando("confiro ")
    public void confiro_os_produtos_contratados() {
        new ResumoContratacaoPage(this.base.getDriver()).continuar();
    }

    @Quando("preencho ")
    public void preencho_os_dados_de_cliente(final String email) {
        this.base.setEmail(email);
        final DadosClientePage dadosCliente = new DadosClientePage(this.base.getDriver()).popupError();
        if (!"Cartao".equalsIgnoreCase(this.base.getProduto()) && !"Ant".equalsIgnoreCase(this.base.getProduto())) {
            dadosCliente.preencherDadosPessoais()
                    .preencherDadosDocumento()
                    .preencherDadosRenda();
        }
        dadosCliente.preencherDadosEndereco()
                .preencherDadosContato(email);
    }

    @Quando("marco ")
    public void marco_mensagem_de_texto_para_envio_de_documentos() {
        new EnvioDocumentosPage(this.base.getDriver()).selecionarMensagemTexto(EnvioDocumentosEnum.MENSAGEM_TEXTO);
        this.base.setCodUrl(new DocumentosDistanciaPage(this.base.getDriver()).reenviarMensagem());
    }

    @Quando("completo ")
    public void completo_checklist() {
        final CheckListPage checkList = new CheckListPage(this.base.getDriver());
        checkList.completarCheckList(this.base.getCpf(), this.base.getCodUrl());

        Assertions.assertTrue(checkList.checkEtapas());
    }

    @Entao("frontend ")
    public void frontend_do_produto_efetivada(final String status) {
        final AprovacaoPage aprovacao = new AprovacaoPage(this.base.getDriver());
        if (!"Cartão ".equals(this.base.getProduto()) && this.base.getEmail().equals("aprova")) {
            new MesaPages(this.base.getDriver()).efetuarLogin().iniciarTarefa().analisarTarefa(this.base.getCpf());
            aprovacao.formalizacaoCamunda(this.base.getEmail(), this.base.getCpf(), 0);

            this.base.getDriver().get(getValue("urlBase"));
            new HomePage(this.base.getDriver()).buscarAtendimento(this.base.getCpf());
        }

        Assertions.assertEquals(aprovacao.checkStatus(status), status);
    }

    @After(value = "@frontend", order = 1)
    public void closeUp(final Scenario scenario) {
        this.base.tearDown(scenario);
    }

}
