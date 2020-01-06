/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import PersonalExceptions.CampoVazioException;
import PersonalExceptions.DividaException;
import PersonalExceptions.InvalidNumeroException;
import PersonalExceptions.NaoEncontradoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.util.InputMismatchException;
import usuarios.User;
import midias.Media;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;
import midias.Local;
import usuarios.Endereco;

/**
 *
 * @author lucas
 */
public class bibliotecario implements Serializable {

    private final LinkedList<Media> midias;  //Aponta para o cabeça de uma lista de midias
    private final LinkedList<User> usuarios;  //Aponta para o cabeça de uma lista de usuarios

    private bibliotecario() {
        midias = new LinkedList();
        usuarios = new LinkedList();
    }

    private void addMidias(Media midias) {  //Adiciona um nó à lista de mídias
        this.midias.add(midias);
    }

    private void addUsuarios(User usuarios) {  //Adiciona um nó à lista de usuarios
        this.usuarios.add(usuarios);
    }

    private Media busca_midia(String substring, User devolucao, int modo) throws NaoEncontradoException {  //Busca na lista de midias, ou na lista de pegos de um usuário (no caso de devolução)
        //Itera a lista para ver se acha, se achar retorna. Se não achar, retorna NULL
        ListIterator<Media> iterador;
        Media achou;
        int indice;
        int opcao;
        Scanner entrada = new Scanner(System.in);

        if (modo == 1) {  //Na lista de midias
            iterador = midias.listIterator();
        } else {  //Na lista de alugados de um usuário
            iterador = devolucao.getMidias_pegas().listIterator();
        }

        while (iterador.hasNext()) {
            achou = iterador.next();
            indice = iterador.previousIndex();
            if ((achou.getTitulo().contains(substring) == true) || (achou.getAutor().contains(substring) == true)) {
                System.out.println(">>Achamos a seguinte mídia: " + achou.getTitulo() + ", " + achou.getAutor() + " - " + achou.getEditora());
                System.out.println("1 - Mídia encontrada");
                System.out.println("2 - Mídia não encontrada, continuar buscando!");
                
                opcao = entrada.nextInt();
                entrada.nextLine();
                
                if (opcao == 1) {  //Finaliza a busca se a opção for 1, mas continua buscando se não for.
                    if (modo == 1) {
                        midias.remove(indice);  //Remove da lista original. O objetivo é passar para a lista de cada usuario (isto é, para a lista de midias alugadas)
                    } else {  //Removendo da lista de pendências.
                        devolucao.getMidias_pegas().remove(indice);
                    }

                    return achou;
                }
            }
        }

        throw new NaoEncontradoException();  //Lança uma exceção para indicar o erro.
    }

    private User busca_user(String substring) throws NaoEncontradoException {  //BUSCAR USANDO O NUSP.
        ListIterator<User> iterador = usuarios.listIterator();
        User achou;

        while (iterador.hasNext()) {
            achou = iterador.next();
            if ((achou.getNusp()).equals(substring) == true) {
                System.out.println(achou.getNome());
                return achou;
            }
        }

        throw new NaoEncontradoException();  //Lança uma exceção para indicar o erro.        
    }

    private boolean checa_user(User user_potencial, int modo) throws DividaException {  //Checa se o usuário (potencial locatário) pode pegar um livro
        if ((user_potencial.getMidias_pegas().isEmpty()) && (modo == 2)) {
            return false;
        }

        ListIterator<Media> iterador = user_potencial.getMidias_pegas().listIterator();
        LocalDate today = LocalDate.now();
        Media atrasada;  //Mídia atrasada (é apenas uma auxiliar)

        while (iterador.hasNext()) {
            atrasada = iterador.next();
            Period Entredatas = Period.between(today, atrasada.getData_locacao());
            if ((Entredatas.getDays() < -7) || (Entredatas.getYears() < -1) || (Entredatas.getMonths() < -1)) {  //Checa a diferença de datas...
                if (modo == 1) {
                    throw new DividaException();
                }
                if (modo == 2) {   //O modo 2 lista as mídias atrasadas de um usuário.  (apesar disso o suário pode realizar a devolução de mídias ainda dentro do prazo)
                    int devendo = -2 * Entredatas.getDays();  //Multa: 2 reais por dia.
                    System.out.println(">>URGENTE: " + atrasada.getTitulo() + ", " + atrasada.getAutor() + ", " + atrasada.getAno());
                    System.out.println("MULTA: " + "R$" + devendo + ",00");
                    System.out.println("-----------------------------------");
                }
            }
        }
        return true;  //Pode pegar mídia
    }

    private void emprestimo(User pedinte, Media obra) {  //Realiza o empréstimo  ---> o user deve possuir um método para indicar se pode pegar ou não
        obra.setData_locacao(LocalDate.now());

        pedinte.getMidias_pegas().add(obra);  //Adiciona a obra para a lista de obras alugadas pelo usuário.
        
        System.out.println("PRONTO!");        
    }

    private void devolucao(Media midia_devolvida) {
        midias.add(midia_devolvida);

        System.out.println("PRONTO!");
    }

    private void listar_acervo() {
        ListIterator<Media> iterador = midias.listIterator();
        Media aux;
        
        System.out.println("----------------ACERVO ATUAL:");

        while (iterador.hasNext()) {
            aux = iterador.next();
            System.out.println(">> " + aux.getTitulo() + ", " + aux.getAutor() + ", " + aux.getAno() + " - " + aux.getEditora());
            System.out.println(">> Tipo: " + aux.getTipo());
            System.out.println(">> Localização: " + aux.getLocalizacao().getSection() + ", " + aux.getLocalizacao().getAndar() + "o andar");
            System.out.println("----------------------------------------------------------");
        }
    }

    private void checa_nusp(String nusp) throws InvalidNumeroException {
        ListIterator<User> iterador = usuarios.listIterator();

        while (iterador.hasNext()) {
            if (iterador.next().getNusp().equals(nusp)) {
                System.out.println("Esse nusp já está cadastrado!");
                throw new InvalidNumeroException();
            }
        }
    }

    public static void main(String[] args) {
        boolean loop = true;
        int opcao;
        Scanner entrada = new Scanner(System.in);
        bibliotecario b = new bibliotecario();
        User novo_usuario;
        Media nova_midia;
        Endereco novo_end;
        LocalDate novo_nasc = null;
        Local novo_local;
        int dia, mes, ano;
        String substring;
        Media midia_potencial;
        User user_potencial;
        boolean entrada_valida = false;

        //LER ESTADO....
        try {
            ObjectInputStream ler = new ObjectInputStream(new FileInputStream("estado.ser"));

            b = (bibliotecario) ler.readObject();
        } catch (IOException ex) {
            System.out.println("O sistema está sem dados!");
        } catch (ClassNotFoundException ex2) {
            System.out.println("Arquivo Inválido!");
            ex2.printStackTrace();
            return;
        }

        while (loop) {  //loop do menu
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Cadastrar Usuario");
            System.out.println("2 - Cadastrar Mídia");
            System.out.println("3 - Alugar Mídia");
            System.out.println("4 - Devolver Mídia");
            System.out.println("5 - Listar Mídias Disponíveis");
            System.out.println("6 - Sair");

            opcao = entrada.nextInt();
            entrada.nextLine();

            switch (opcao) {
                case 1:  //Cadastra novo usuário (vale para todas as entradas abaixo...)
                    novo_usuario = new User();
                    novo_end = new Endereco();

                    do {  //Se lançar uma exceção, continua no loop
                        try {
                            System.out.println("Insira seu nome:");
                            novo_usuario.setNome(entrada.nextLine());
                            entrada_valida = true;
                        } catch (CampoVazioException a) {
                            System.out.println("##É necessário inserir um nome!");
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    do {
                        try {
                            System.out.println("Insira seu número USP");

                            substring = entrada.nextLine();

                            b.checa_nusp(substring);  //Checa se o usuário já está cadastrado
                            novo_usuario.setNusp(substring);
                            entrada_valida = true;
                        } catch (InvalidNumeroException d) {
                            System.out.println("##Nusp inválido! - lembre-se, deve conter exatamente sete dígitos e não pode ter sido cadastrado anteriormente");
                        } catch (CampoVazioException a) {
                            System.out.println("##É necessário inserir um nusp!");
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    entrada.useDelimiter("/|\n");

                    do {
                        System.out.println("Insira sua data de nascimento (dia/mes/ano):");
                        entrada.hasNext();

                        dia = entrada.nextInt();
                        mes = entrada.nextInt();
                        ano = entrada.nextInt();

                        try {
                            novo_nasc = LocalDate.of(ano, mes, dia);
                            entrada_valida = true;
                        } catch (DateTimeException e) {
                            System.out.println("##Nascimento inválido!");
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;
                    entrada.useDelimiter(", |\n");

                    do {
                        try {
                            System.out.println("Insira seu endereço (rua, numero, cidade):");
                            entrada.hasNext();  //!!!Fazer método para ver se o nusp é único

                            novo_end.setRua(entrada.next());
                            novo_end.setNumero(entrada.nextInt());
                            novo_end.setCidade(entrada.next());
                            entrada_valida = true;

                        } catch (InputMismatchException | InvalidNumeroException f) {
                            System.out.println("Número inválido");
                            entrada.nextLine();
                        } catch (CampoVazioException a) {
                            System.out.println("É preciso inserir o endereço completo para continuar!");
                            entrada.nextLine();
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    novo_usuario.setEndereco(novo_end);
                    novo_usuario.setNascimento(novo_nasc);

                    b.addUsuarios(novo_usuario);
                    break;
                case 2:  //Cadastra nova midia
                    novo_local = new Local();
                    nova_midia = new Media();

                    do {
                        try {
                            System.out.println("Insira o titulo da mídia:");
                            nova_midia.setTitulo(entrada.nextLine());
                            entrada_valida = true;
                        } catch (CampoVazioException h) {
                            System.out.println("É preciso preencher esse campo para continuar!");
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    do {
                        try {
                            System.out.println("Insira o nome do autor:");
                            nova_midia.setAutor(entrada.nextLine());
                            entrada_valida = true;
                        } catch (CampoVazioException h) {
                            System.out.println("É preciso preencher esse campo para continuar!");
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    do {
                        try {
                            System.out.println("Insira o nome da editora:");
                            nova_midia.setEditora(entrada.nextLine());
                            entrada_valida = true;
                        } catch (CampoVazioException h) {
                            System.out.println("É preciso preencher esse campo para continuar!");
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    do {
                        try {
                            System.out.println("Insira o ano de publicação:");
                            nova_midia.setAno(entrada.nextInt());
                            entrada.nextLine();
                            entrada_valida = true;
                        } catch (InvalidNumeroException i) {
                            System.out.println("Ano inválido!");
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    do {
                        try {
                            System.out.println("Insira o tipo (revista, livro, tese)");
                            nova_midia.setTipo(entrada.nextLine());
                            entrada_valida = true;
                        } catch (CampoVazioException h) {
                            System.out.println("É preciso preencher esse campo para continuar!");
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    entrada.useDelimiter(", |\n");

                    do {
                        try {
                            System.out.println("Insira a localização (seção, andar):");
                            entrada.hasNext();

                            novo_local.setSection(entrada.next());
                            novo_local.setAndar(entrada.nextInt());
                            entrada_valida = true;
                        } catch (CampoVazioException h) {
                            System.out.println("É preciso preencher esse campo para continuar!");
                            entrada.nextLine();
                        } catch (InvalidNumeroException i) {
                            System.out.println("Andar inexistente!");
                            entrada.nextLine();
                        }
                    } while (!entrada_valida);

                    entrada_valida = false;

                    nova_midia.setLocalizacao(novo_local);

                    b.addMidias(nova_midia);
                    break;
                case 3:  //Faz aluguel
                    try {
                        System.out.println("Insira o NUSP do usuário que fará a locação:");
                        substring = entrada.nextLine();
                        if (substring.equals("")) {
                            System.out.println("É necessário informar o nusp para continuar!");
                            break;
                        }

                        user_potencial = b.busca_user(substring);   //Se não tiver tal user, lança uma exceção

                        b.checa_user(user_potencial, 1);    //Se o user não puder pegar, lança uma exceção.

                        System.out.println("Insira o nome da mídia que deseja alugar (ou o nome do autor):");
                        substring = entrada.nextLine();
                        if (substring.equals("")) {
                            System.out.println("É necessário informar o nome da mídia para continuar!");
                            break;
                        }

                        midia_potencial = b.busca_midia(substring, null, 1);   //Se não tiver nenhuma exceção, o livro está disponivel na biblioteca

                        b.emprestimo(user_potencial, midia_potencial);
                        entrada_valida = true;
                    } catch (NaoEncontradoException z) {  //Nesse caso, temos uma exceção para indicar que o sistema não achou a mídia/user
                        System.out.println("Mídia ou usuário não encontrado!");
                    } catch (DividaException k) {  //Para indicar que o usuário está em divida, e não pode alugar uma mídia
                        System.out.println("Usuário em dívida! Devolva a mídia e pague a multa antes de pegar outra.");
                    }

                    break;
                case 4:  //Faz Devolução
                    try {
                        System.out.println("Insira o NUSP do usuário que fará a devolução:");
                        substring = entrada.nextLine();

                        user_potencial = b.busca_user(substring);

                        if (!b.checa_user(user_potencial, 2)) {  //Lista as mídias com devolução pendente.
                            System.out.println("Esse usuário não alugou nenhum livro!");
                            break;
                        }

                        System.out.println("Insira o nome da mídia que deseja devolver (e pagar a multa):");
                        substring = entrada.nextLine();

                        midia_potencial = b.busca_midia(substring, user_potencial, 2);

                        b.devolucao(midia_potencial);
                    } catch (NaoEncontradoException z) {  //Nesse caso, temos uma exceção para indicar que o sistema não achou a mídia/user
                        System.out.println("Mídia ou usuário não encontrado!");
                    } catch (DividaException k) {  //Para indicar que o usuário está em divida, e não pode alugar uma mídia
                        System.out.println("Usuário em dívida! Devolva a mídia atrasada e pague a multa antes de pegar outra.");
                    }

                    break;
                case 5:  //Lista o acervo de midias disponíveis
                    b.listar_acervo();

                    break;
                case 6:
                    loop = false;
                    break;
            }
        }

        try {
            try (ObjectOutputStream escrever = new ObjectOutputStream(new FileOutputStream("estado.ser"))) {
                escrever.writeObject(b);
            }
        } catch (IOException ex) {
            System.out.println("Erro!!");
            ex.printStackTrace();
        }
    }

}
