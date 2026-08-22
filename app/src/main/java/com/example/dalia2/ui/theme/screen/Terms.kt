package com.example.dalia2.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dalia2.ui.theme.Dalia2Theme

@Composable
fun TermsAndConditionsScreen(
    onBackClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Barra Superior (Botão de voltar + Título)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Voltar",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onBackClick() }
                )

                Text(
                    text = "Termos & Condições",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 1
            Text(
                text = "1. Disposições gerais e aceitação",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "1.1. Este Termo de Uso (“Termo”) regula a utilização do aplicativo Dália (“Plataforma”), um software de calendário menstrual que oferece, adicionalmente, recursos de fórum de discussão e ferramentas para denúncia de violência contra a mulher.\n" +
                        "1.2. Ao baixar, acessar ou utilizar a Plataforma, o usuário declara ter lido, compreendido e aceitado integralmente todas as cláusulas e condições deste Termo, bem como da Política de Privacidade que lhe é complementar.\n" +
                        "1.3. Caso não concorde com qualquer disposição deste Termo, o usuário deve interromper imediatamente o uso da Plataforma.\n" +
                        "1.4. Este é um Termo de Uso, aplicável durante a fase inicial de funcionamento da Plataforma, podendo ser substituído por versão definitiva a qualquer tempo, nos termos da cláusula 11.",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 2
            Text(
                text = "2. Objetivo e finalidade",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "2.1. A Plataforma tem como objetivos principais:\n" +
                        "(a) Permitir o registro, acompanhamento e previsão do ciclo menstrual da usuária;\n" +
                        "(b) Oferecer um espaço de fórum para troca de informações, experiências e apoio entre usuárias;\n" +
                        "(c) Disponibilizar canal para denúncia de situações de violência contra a mulher, com informações sobre redes de acolhimento e canais oficiais de atendimento.\n" +
                        "2.2. A Plataforma não substitui consultas médicas, diagnósticos ou tratamentos profissionais. As informações fornecidas têm caráter meramente informativo e de apoio.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 3
            Text(
                text = "3. Cadastro e acesso",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "3.1. O uso da Plataforma exige cadastro prévio, com fornecimento de dados verdadeiros, precisos e atualizados, tais como nome, e-mail, data de nascimento e outros dados necessários ao funcionamento do calendário menstrual.\n" +
                        "3.2. O usuário é único e exclusivo responsável pela guarda e sigilo de sua senha e credenciais de acesso, não devendo compartilhá-las com terceiros.\n" +
                        "3.3. O cadastro é pessoal e intransferível. O compartilhamento de conta é vedado.\n" +
                        "3.4. Menores de 18 (dezoito) anos somente poderão utilizar a Plataforma com autorização expressa de seus responsáveis legais.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 4
            Text(
                text = "4. Do fórum e conteúdo gerado pelo usuário",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "4.1. A Plataforma disponibiliza um Fórum onde os usuários podem publicar mensagens, comentários, perguntas, relatos e outros conteúdos (“Conteúdo do Usuário”).\n" +
                        "4.2. O usuário é integralmente responsável por todo o Conteúdo que publicar no Fórum, respondendo civil e criminalmente por eventuais danos causados a terceiros ou à Plataforma.\n" +
                        "4.3. Ao publicar Conteúdo no Fórum, o usuário concede à Plataforma licença não exclusiva, irrevogável, mundial e gratuita para usar, reproduzir, modificar, adaptar, publicar e distribuir tal conteúdo, no âmbito das funcionalidades da Plataforma.\n" +
                        "4.4. É terminantemente proibido no Fórum:\n" +
                        "(a) Publicar conteúdo ofensivo, difamatório, calunioso, injurioso, discriminatório ou que incite violência;\n" +
                        "(b) Compartilhar informações falsas ou enganosas;\n" +
                        "(c) Praticar assédio, perseguição (“stalking”) ou qualquer forma de violência psicológica contra outros usuários;\n" +
                        "(d) Publicar conteúdos de natureza pornográfica, obscena ou sexualmente explícita;\n" +
                        "(e) Divulgar dados pessoais de terceiros sem autorização;\n" +
                        "(f) Utilizar o Fórum para fins comerciais, publicitários ou de captação de usuários para outros serviços;\n" +
                        "(g) Praticar qualquer ato que viole a Lei Maria da Penha (Lei nº 11.340/2006) ou a legislação penal.\n" +
                        "4.5. A Plataforma se reserva o direito de moderar, editar, ocultar ou remover qualquer Conteúdo do Usuário que viole este Termo, a legislação aplicável ou a moral, independentemente de aviso prévio.\n" +
                        "4.6. A Plataforma não endossa, não garante e não se responsabiliza pela veracidade, precisão ou confiabilidade de qualquer Conteúdo do Usuário publicado no Fórum.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 5
            Text(
                text = "5. Recursos de denúncia e violência",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "5.1. A Plataforma disponibiliza um recurso específico para que usuárias possam registrar denúncias de situações de violência contra a mulher, bem como obter informações sobre canais oficiais de acolhimento e atendimento.\n" +
                        "5.2. Ao utilizar o recurso de denúncia, a usuária poderá:\n" +
                        "(a) Registrar relato da situação de violência (física, psicológica, sexual, patrimonial ou moral);\n" +
                        "(b) Anexar documentos, imagens ou outros elementos de prova, se assim desejar;\n" +
                        "(c) Receber orientações sobre como buscar ajuda junto a órgãos competentes;\n" +
                        "(d) Ser direcionada aos canais oficiais, como o Ligue 180 (Central de Atendimento à Mulher), que funciona 24 horas por dia, todos os dias da semana.\n" +
                        "5.3. A Plataforma NÃO substitui os canais oficiais de denúncia, como delegacias, Ministério Público, Defensoria Pública ou o Ligue 180. O recurso de denúncia tem caráter informativo e de apoio, não gerando, por si só, qualquer efeito jurídico ou policial.\n" +
                        "5.4. A Plataforma se compromete a:\n" +
                        "(a) Tratar os relatos de violência com sigilo absoluto e respeitando a intimidade da vítima;\n" +
                        "(b) Não compartilhar as informações da denúncia com terceiros, salvo por determinação judicial ou legal;\n" +
                        "(c) Manter a usuária informada sobre os recursos disponíveis e os passos seguintes para busca de ajuda.\n" +
                        "5.5. Em situações de emergência ou risco iminente de vida, a usuária deve acionar imediatamente a Polícia Militar pelo 190 ou dirigir-se à delegacia mais próxima.\n" +
                        "5.6. A Plataforma poderá, a seu critério, comunicar denúncias de violência grave às autoridades competentes, sempre resguardando a identidade e a segurança da vítima.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 6
            Text(
                text = "6. Dados Pessoais e Privaciade",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "6.1. A Plataforma coleta e trata dados pessoais dos usuários, incluindo dados sensíveis relacionados à saúde menstrual, nos termos da Lei Geral de Proteção de Dados (LGPD – Lei nº 13.709/2018).\n" +
                        "6.2. Os dados coletados serão utilizados exclusivamente para as finalidades descritas neste Termo e na Política de Privacidade, quais sejam:\n" +
                        "(a) Funcionamento do calendário menstrual e previsões;\n" +
                        "(b) Personalização da experiência do usuário;\n" +
                        "(c) Envio de notificações e lembretes (mediante consentimento);\n" +
                        "(d) Moderar o Fórum e garantir a segurança da comunidade;\n" +
                        "(e) Aprimorar os serviços da Plataforma.\n" +
                        "6.3. Os dados pessoais não serão compartilhados com terceiros, exceto:\n" +
                        "(a) Com o consentimento expresso do usuário;\n" +
                        "(b) Por determinação judicial ou legal;\n" +
                        "(c) Para proteção da vida ou da integridade física da usuária ou de terceiros.\n" +
                        "6.4. O usuário tem direito a:\n" +
                        "(a) Acessar, corrigir ou atualizar seus dados pessoais;\n" +
                        "(b) Solicitar a exclusão de seus dados, nos termos da LGPD;\n" +
                        "(c) Revogar consentimentos anteriormente concedidos.\n" +
                        "6.5. A Plataforma adota medidas técnicas e organizacionais adequadas para proteger os dados pessoais contra acessos não autorizados, perda, destruição ou alteração.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 7
            Text(
                text = "7. Obrigações e responsabilidades do usuário",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "7.1. O usuário se compromete a:\n" +
                        "(a) Utilizar a Plataforma de boa-fé e em conformidade com a legislação aplicável;\n" +
                        "(b) Fornecer informações verdadeiras e atualizadas;\n" +
                        "(c) Não praticar qualquer ato que comprometa a segurança, a integridade ou o funcionamento da Plataforma;\n" +
                        "(d) Respeitar os demais usuários e a moderação do Fórum;\n" +
                        "(e) Não utilizar a Plataforma para fins ilícitos, imorais ou contrários à ordem pública.\n" +
                        "7.2. O usuário é responsável por toda atividade realizada em sua conta, respondendo por danos causados à Plataforma ou a terceiros em decorrência de seu uso.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 8
            Text(
                text = "8. Limitações de responsabilidade",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "8.1. A Plataforma é disponibilizada “no estado em que se encontra” (“as is”), sem garantias de disponibilidade contínua, ausência de erros ou compatibilidade com todos os dispositivos.\n" +
                        "8.2. A Plataforma não se responsabiliza por:\n" +
                        "(a) Danos decorrentes de interpretações equivocadas das informações fornecidas pelo calendário menstrual ou pelo Fórum;\n" +
                        "(b) Conteúdos publicados por usuários no Fórum;\n" +
                        "(c) Interrupções temporárias do serviço por razões técnicas, manutenção ou força maior;\n" +
                        "(d) Perda de dados decorrente de falhas técnicas, desde que observadas as melhores práticas de segurança.\n" +
                        "8.3. Em nenhuma hipótese a Plataforma será responsável por danos morais, materiais ou lucros cessantes decorrentes do uso ou da impossibilidade de uso da Plataforma, salvo nos casos de dolo ou culpa grave devidamente comprovados.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 9
            Text(
                text = "9. Propriedade intelectual",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "9.1. Todos os direitos de propriedade intelectual sobre a Plataforma, incluindo código-fonte, design, marcas, logotipos, textos, imagens e demais elementos, pertencem aos seus desenvolvedores ou licenciantes.\n" +
                        "9.2. O usuário não adquire qualquer direito de propriedade sobre a Plataforma, tendo apenas uma licença de uso pessoal, não comercial, intransferível e revogável.\n" +
                        "9.3. É vedado ao usuário:\n" +
                        "(a) Copiar, reproduzir, distribuir ou modificar qualquer parte da Plataforma sem autorização;\n" +
                        "(b) Realizar engenharia reversa, descompilar ou tentar extrair o código-fonte;\n" +
                        "(c) Utilizar a Plataforma para criar serviços concorrentes.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 10
            Text(
                text = "10. Canais de ajuda e denúncia oficiais",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "10.1. A Plataforma reforça que, em caso de violência contra a mulher, os seguintes canais oficiais estão disponíveis:\n" +
                        "Ligue 180 – Central de Atendimento à Mulher (24 horas, todos os dias);\n" +
                        "190 – Polícia Militar (emergências);\n" +
                        "Delegacia da Mulher mais próxima;\n" +
                        "Defensoria Pública e Ministério Público estaduais.\n" +
                        "10.2. A Plataforma incentiva todas as usuárias a conhecerem a Lei Maria da Penha (Lei nº 11.340/2006) e a buscarem informações sobre seus direitos.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 11
            Text(
                text = "11. Alterações deste termo",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "11.1. A Plataforma reserva-se o direito de alterar este Termo de Uso a qualquer momento, especialmente para adequá-lo a alterações legislativas, jurisprudenciais ou para aprimoramento dos serviços.\n" +
                        "11.2. As alterações serão comunicadas aos usuários por meio da própria Plataforma, por e-mail ou por notificação no aplicativo.\n" +
                        "11.3. O uso continuado da Plataforma após a comunicação das alterações implicará aceitação tácita do novo Termo.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 12
            Text(
                text = "12. Suspensão e recisão",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "12.1. A Plataforma poderá, a qualquer momento e sem aviso prévio:\n" +
                        "(a) Suspender ou cancelar a conta do usuário que violar este Termo ou a legislação;\n" +
                        "(b) Remover conteúdos inadequados do Fórum;\n" +
                        "(c) Bloquear o acesso de usuários que pratiquem assédio, discriminação ou qualquer forma de violência contra outros usuários.\n" +
                        "12.2. O usuário poderá cancelar sua conta a qualquer momento, solicitando a exclusão de seus dados pessoais, nos termos da LGPD.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Parágrafo 13
            Text(
                text = "13. Disposições finais",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "13.1. Este Termo é regido pela legislação brasileira, em especial pela Lei nº 13.709/2018 (LGPD), pela Lei nº 11.340/2006 (Lei Maria da Penha) e pelo Código Civil Brasileiro.\n" +
                        "13.2. O foro da comarca de [CIDADE/ESTADO] será competente para dirimir quaisquer controvérsias decorrentes deste Termo, com renúncia expressa a qualquer outro, por mais privilegiado que seja.\n" +
                        "13.3. Caso qualquer cláusula deste Termo seja considerada inválida ou ineficaz, as demais cláusulas permanecerão em pleno vigor e efeito.\n",
                fontSize = 14.sp,
                color = Color.Black.copy(alpha = 0.8f),
                lineHeight = 22.sp
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAndConditionsScreenPreview() {
    Dalia2Theme {
        TermsAndConditionsScreen()
    }
}