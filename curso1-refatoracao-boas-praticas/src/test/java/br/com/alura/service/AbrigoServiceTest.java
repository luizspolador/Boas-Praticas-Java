package br.com.alura.service;

import br.com.alura.client.ClientHttpConfiguration;
import br.com.alura.domain.Abrigo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.http.HttpResponse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbrigoServiceTest {
    // Cria uma instância mock de ClientHttpConfiguration para simular o comportamento da classe real.
    private ClientHttpConfiguration client = mock(ClientHttpConfiguration.class);

    // Cria uma instância de AbrigoService, passando o client simulado.
    private AbrigoService abrigoService = new AbrigoService(client);

    // Cria uma instância mock de HttpResponse para simular a resposta HTTP.
    private HttpResponse<String> response = mock(HttpResponse.class);

    // Cria uma instância de Abrigo para usar nos testes.
    private Abrigo abrigo = new Abrigo("Teste", "61981880392", "abrigo_alura@gmail.com");

    @Test
    public void deveVerificarQuandoHaAbrigo() throws IOException, InterruptedException {
        // Define um ID para o abrigo como 0L.
        abrigo.setId(0L);

        // Strings esperadas que serão comparadas com a saída no console.
        String expectedAbrigosCadastrados = "Abrigos cadastrados:";
        String expectedIdENome = "0 - Teste";

        // Cria uma saída de console personalizada para capturar a saída do método testado.
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        // Quando o método 'body()' da resposta HTTP for chamado, ele retornará uma representação em JSON do abrigo.
        when(response.body()).thenReturn("[{" + abrigo.toString() + "}]");

        // Quando o método 'dispararRequisicaoGet' do cliente for chamado com qualquer string como argumento, ele retornará a resposta simulada.
        when(client.dispararRequisicaoGet(anyString())).thenReturn(response);

        // Chama o método 'listarAbrigo' do serviço, que deve imprimir a lista de abrigos no console.
        abrigoService.listarAbrigo();

        // Divide a saída do console em linhas.
        String[] lines = baos.toString().split(System.lineSeparator());

        // Captura as linhas de saída para comparar com as strings esperadas.
        String actualAbrigosCadastrados = lines[0];
        String actualIdENome = lines[1];

        // Realiza as asserções para verificar se a saída do método corresponde ao esperado.
        Assertions.assertEquals(expectedAbrigosCadastrados, actualAbrigosCadastrados);
        Assertions.assertEquals(expectedIdENome, actualIdENome);
    }

    @Test
    public void deveVerificarQuandoNaoHaAbrigo() throws IOException, InterruptedException {
        abrigo.setId(0L);
        String expected = "Não há abrigos cadastrados";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);
        System.setOut(printStream);

        when(response.body()).thenReturn("[]");
        when(client.dispararRequisicaoGet(anyString())).thenReturn(response);

        abrigoService.listarAbrigo();

        String[] lines = baos.toString().split(System.lineSeparator());
        String actual = lines[0];

        Assertions.assertEquals(expected, actual);
    }
}
