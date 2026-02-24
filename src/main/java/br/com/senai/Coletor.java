package br.com.senai;

// Importa as classes necessárias da biblioteca Eclipse Paho para comunicação MQTT
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Coletor {

    // O 'throws MqttException' avisa ao Java que este bloco pode gerar erros de conexão com a rede
    public static void main(String[] args) throws MqttException {

        // Define o endereço do servidor (Broker) público do HiveMQ e a porta padrão (1883)
        String broker = "tcp://broker.hivemq.com:1883";
        // Define o tópico onde o programa vai "escutar" os dados enviados pelo hardware (ESP32)
        String topico = "senai/andre/motor/dados";
        // Cria o cliente MQTT passando o servidor e um ID único (nome) para esta máquina
        MqttClient client = new MqttClient(broker, "JavaBackend_Andre");
        
        System.out.println("Conectando ao Broker...");
        // Inicia efetivamente a conexão com o servidor na internet
        client.connect(); 
        System.out.println("Conectado com sucesso!");
        System.out.println("Aguardando dados do ESP32 no tópico: " + topico);
        
        // "Assina" (inscreve-se) no tópico. A função Lambda '->' dita o que fazer quando a mensagem chegar
        client.subscribe(topico, (topic, msg) -> {

            // Pega o conteúdo da mensagem (que vem em bytes) e converte para texto legível (String)
            String payload = new String(msg.getPayload());

            // String payload = "35.5,12.3,8.9"; // (Linha mantida como histórico de testes manuais locais)

            // O comando split(",") divide o texto toda vez que acha uma vírgula, criando um Array (lista) de dados
            String[] dados = payload.split(",");

            // Verifica se chegaram exatamente 3 informações (garantindo que o pacote não veio quebrado ou vazio)
            if (dados.length == 3) {
                // Guarda cada pedaço fatiado na sua respectiva variável, de acordo com a ordem enviada
                String temp = dados[0];
                String vibra = dados[1];
                String corrente = dados[2];

                // Imprime as informações no console já formatadas com suas unidades de medida
                System.out.println("\n=-= Dados coletados com sucesso =-=");
                System.out.println("Temperatura: " + temp + " °C");
                System.out.println("Corrente: " + corrente + " A");
                System.out.println("Vibração: " + vibra + " mm/s");
            }
        });
    }
}