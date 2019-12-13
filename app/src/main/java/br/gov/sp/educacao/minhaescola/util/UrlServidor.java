package br.gov.sp.educacao.minhaescola.util;

public class UrlServidor {

    public static final String URL_SERVIDOR = "https://sed.educacao.sp.gov.br";

    public static final String URL_LOGIN = URL_SERVIDOR + "/SedApi/Api/Login";

    public static final String URL_SELECIONAR_PERFIL = URL_SERVIDOR + "/SedApi/Api/Login/SelecionarPerfil";

    public static final String URL_BUSCAR_ALUNOS = URL_SERVIDOR + "/SedApi/Api/Usuario";

    public static final String URL_ENVIAR_ID_PUSH = URL_SERVIDOR + "/SedApi/Api/mensageria/cadastrarDispositivo";

    public static final String URL_BUSCAR_HORARIOS = URL_SERVIDOR + "/SedApi/Api/HorarioAula";
    
    public static final String URL_GERAR_BOLETIM = URL_SERVIDOR + "/Boletim/BoletimEscolar";

    public static final String URL_GERAR_BOLETIM_UNIFICADO = URL_SERVIDOR + "/Boletim/GerarBoletimUnificadoExterno";

    public static final String URL_BUSCAR_CARTEIRINHA = URL_SERVIDOR + "/SedApi/Api/CarteirinhaAluno";

    public static final String URL_ENVIAR_FOTO = URL_SERVIDOR + "/FotoDoAluno/Api/Photo";

    public static final String URL_BUSCAR_NOTAS_FREQUENCIA = URL_SERVIDOR + "/SedApi/Api/Boletim/GerarAlunoTurma";

    public static final String URL_PODE_RESPONDER = URL_SERVIDOR + "/SedApi/Api/AlimentacaoEscolar/PodeResponder";

    public static final String URL_ENVIAR_AVALIACAO_ALIMENTACAO = URL_SERVIDOR + "/SedApi/Api/AlimentacaoEscolar/Salvar";

    public static final String URL_CALENDARIO = URL_SERVIDOR + "/SedApi/Api/CalendarioEscolar/EventosCalendario?codigoTurma=";

    public static final String URL_REMATRICULA = URL_SERVIDOR + "/SedApi/Api/FichaAluno/Alunos";

    public static final boolean IS_DEBUG = false;

    public static String URLSERVIDOR_TRACKER = "https://sed.educacao.sp.gov.br";
}
