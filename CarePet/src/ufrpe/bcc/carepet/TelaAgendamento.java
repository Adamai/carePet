package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class TelaAgendamento extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField textDia;
	private JTextField textMes;
	private JTextField textAno;
	private JTextField textHora;
	private JTextField FieldCPF;
	private JButton btnCriarAgendamento;
	private JButton btnConsultarAnimais;
	private JTextField FieldCodServ;
	private JButton btnChecarServ;
	private JTextArea txtrDescrip;
	private JLabel lblTipoServ;
	private ResultSet rsTipoServ;
	private JTextField textObs;
	private JTextField textPrice;
	private DefaultListModel model;
	private JButton btnVoltar;
	private Funcionario funcionario;
	private JTextField textHoraF;
	private JComboBox comboTipo;
	private JList list;
	private JLabel lblMotivo;
	private JTextField textCliMotivo;
	private JTextField textLabNome;
	private JTextField textLabTel;
	private JLabel lblTelefone;
	private JLabel lblNomeDoLa;
	private JScrollPane scrollPane;
	private JList listCli = new JList();
	private DefaultListModel modelCl = new DefaultListModel();

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaAgendamento frame = new TelaAgendamento();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	
static {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("Problemas carregando o Driver do MySQL");
		}
	}
	
	public static Connection getConexao() throws SQLException {
		
		Connection retorno = null;
		retorno = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/carepet?autoReconnect=true&useSSL=false", "root", "paloma"); // nome do esquema, usuário e senha
		return retorno;
	}
	
	public TelaAgendamento(Funcionario func) {
		funcionario = func;
		setTitle("CarePet - Agendamento");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblLogadoComo = new JLabel("Logado como: "+func.getNome());
		
		int ilista = 0;
		try{
			Connection conex = getConexao();
			String query = "	SELECT * FROM cliente;";
			PreparedStatement psGetCli = conex.prepareStatement(query);
			ResultSet rsGetCli = psGetCli.executeQuery();
			while(rsGetCli.next()){
				modelCl.addElement(rsGetCli.getString("nome"));
				ilista++;
			}
			psGetCli.close();
			conex.close();
		} catch (SQLException e) {
			System.out.println("Houve erro");
			e.printStackTrace();
		}
		if(ilista!=0)
			listCli.setModel(modelCl);
		
		listCli.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) { 
				try{
	      			Connection conex = getConexao();
	      			PreparedStatement pscpf = conex.prepareStatement("SELECT CPF FROM cliente where nome='"+listCli.getSelectedValue().toString()+"';");
	      			ResultSet rscpf = pscpf.executeQuery();
	      			rscpf.next();
	      			FieldCPF.setText(rscpf.getString("CPF"));
				} catch (SQLException e) {
	      			System.out.println("Houve erro");
	      			e.printStackTrace();
	      		}
	        }
		});
		
    	
		String[] TiposAtend = {"CLI", "LAB", "PET"};
		comboTipo = new JComboBox(TiposAtend);
		comboTipo.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        if(comboTipo.getSelectedItem().toString()=="CLI"){
		        	textCliMotivo.setVisible(true);
		        	lblMotivo.setVisible(true);
		        	lblNomeDoLa.setVisible(false);
		        	textLabNome.setVisible(false);
		        	lblTelefone.setVisible(false);
		        	textLabTel.setVisible(false);
		        }
		        if(comboTipo.getSelectedItem().toString()=="LAB"){
		        	textCliMotivo.setVisible(false);
		        	lblMotivo.setVisible(false);
		        	lblNomeDoLa.setVisible(true);
		        	textLabNome.setVisible(true);
		        	lblTelefone.setVisible(true);
		        	textLabTel.setVisible(true);
		        }
		        if(comboTipo.getSelectedItem().toString()=="PET"){
		        	textCliMotivo.setVisible(false);
		        	lblMotivo.setVisible(false);
		        	lblNomeDoLa.setVisible(false);
		        	textLabNome.setVisible(false);
		        	lblTelefone.setVisible(false);
		        	textLabTel.setVisible(false);
		        }
		    }
		});
		
		comboTipo.setToolTipText("Tipo do Agendamento");
		
		JLabel lblTipoDeAgendamento = new JLabel("Tipo de Agendamento:");
		
		JLabel lblData = new JLabel("Data(dd/mm/aa):");
		
		textDia = new JTextField();
		textDia.setColumns(10);
		
		textMes = new JTextField();
		textMes.setColumns(10);
		
		textAno = new JTextField();
		textAno.setColumns(10);
		
		JLabel lblHorario = new JLabel("Horario:");
		
		textHora = new JTextField();
		textHora.setText("00:00");
		textHora.setColumns(10);
		
		FieldCPF = new JTextField();
		FieldCPF.setColumns(10);
		
		JLabel lblCpfCliente = new JLabel("CPF Cliente");
		model = new DefaultListModel();
		list = new JList(model);
		
		btnCriarAgendamento = new JButton("Criar Agendamento");
		
		btnConsultarAnimais = new JButton("Consultar Animais");
		
		btnCriarAgendamento.addActionListener(this);
		btnConsultarAnimais.addActionListener(this);
		
		lblTipoServ = new JLabel("Tipo de Servi\u00E7o:");
		
		JLabel lblCodServ = new JLabel("Cod. de Servi\u00E7o:");
		
		FieldCodServ = new JTextField();
		FieldCodServ.setColumns(10);
		
		txtrDescrip = new JTextArea();
		txtrDescrip.setText("Descri\u00E7\u00E3o");
		
		btnChecarServ = new JButton("Checar Servi\u00E7os");
		btnChecarServ.addActionListener(this);
		
		JLabel lblObs = new JLabel("Obs:");
		
		textObs = new JTextField();
		textObs.setColumns(10);
		
		JLabel lblPrice = new JLabel("Pre\u00E7o:");
		
		textPrice = new JTextField();
		textPrice.setText("000.00");
		textPrice.setColumns(10);
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		JLabel lblHorarioF = new JLabel("Hor\u00E1rio previsto para t\u00E9rmino:");
		
		textHoraF = new JTextField();
		textHoraF.setText("00:00");
		textHoraF.setColumns(10);
		
		lblMotivo = new JLabel("Motivo (Apenas para atendimento CLI)");
		
		textCliMotivo = new JTextField();
		textCliMotivo.setColumns(10);
		
		textLabNome = new JTextField();
		textLabNome.setColumns(10);
		
		textLabTel = new JTextField();
		textLabTel.setColumns(10);
		
		lblTelefone = new JLabel("Telefone:");
		
		lblNomeDoLa = new JLabel("Nome do Laborat\u00F3rio (LAB)");
		
		scrollPane = new JScrollPane();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLogadoComo))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(btnVoltar)
					.addContainerGap(1013, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(39)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(lblMotivo, GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
							.addGap(475)
							.addComponent(btnCriarAgendamento, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(textCliMotivo, GroupLayout.PREFERRED_SIZE, 307, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addComponent(lblNomeDoLa)
								.addComponent(lblTelefone))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(textLabTel, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)
								.addComponent(textLabNome, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(324, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
									.addComponent(textObs)
									.addGroup(gl_contentPane.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
												.addGroup(gl_contentPane.createSequentialGroup()
													.addComponent(lblTipoDeAgendamento)
													.addGap(34)
													.addComponent(comboTipo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addGroup(gl_contentPane.createSequentialGroup()
													.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addComponent(lblData)
														.addComponent(lblHorario))
													.addPreferredGap(ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
													.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_contentPane.createSequentialGroup()
															.addComponent(textDia, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
															.addPreferredGap(ComponentPlacement.RELATED)
															.addComponent(textMes, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
															.addPreferredGap(ComponentPlacement.RELATED)
															.addComponent(textAno, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
														.addComponent(textHora, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
											.addComponent(lblTipoServ)
											.addGroup(gl_contentPane.createSequentialGroup()
												.addComponent(lblCodServ)
												.addGap(18)
												.addComponent(FieldCodServ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
											.addGroup(gl_contentPane.createSequentialGroup()
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnChecarServ))
											.addGroup(gl_contentPane.createSequentialGroup()
												.addGap(33)
												.addComponent(lblHorarioF)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(textHoraF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
									.addComponent(txtrDescrip, GroupLayout.DEFAULT_SIZE, 641, Short.MAX_VALUE))
								.addComponent(textPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblPrice)
								.addComponent(lblObs))
							.addPreferredGap(ComponentPlacement.RELATED, 136, Short.MAX_VALUE)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
									.addGap(23)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
										.addGroup(gl_contentPane.createSequentialGroup()
											.addComponent(list, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED))
										.addGroup(gl_contentPane.createSequentialGroup()
											.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
												.addGroup(gl_contentPane.createSequentialGroup()
													.addComponent(lblCpfCliente)
													.addGap(18)
													.addComponent(FieldCPF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addComponent(btnConsultarAnimais))
											.addGap(56)))
									.addGap(10))
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)
									.addGap(55))))))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLogadoComo)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboTipo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTipoDeAgendamento))
					.addGap(3)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblData)
								.addComponent(textAno, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textMes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(textDia, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblHorario)
								.addComponent(textHora, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblHorarioF)
								.addComponent(textHoraF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addComponent(lblTipoServ)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(FieldCodServ, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCodServ)
								.addComponent(btnChecarServ))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtrDescrip, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblObs)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textObs, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblPrice)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(textPrice, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(118)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(FieldCPF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCpfCliente))
							.addGap(15)
							.addComponent(btnConsultarAnimais)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(list, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(33)
							.addComponent(lblMotivo)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(textCliMotivo, GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGap(55)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblNomeDoLa)
										.addComponent(textLabNome, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(textLabTel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblTelefone))))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnVoltar))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(14)
							.addComponent(btnCriarAgendamento)
							.addGap(162))))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(31)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(382, Short.MAX_VALUE))
		);
		
		
		scrollPane.setViewportView(listCli);
		contentPane.setLayout(gl_contentPane);
    	lblNomeDoLa.setVisible(false);
    	textLabNome.setVisible(false);
    	lblTelefone.setVisible(false);
    	textLabTel.setVisible(false);
	}
	
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnChecarServ)){
			try{
				Connection conex = getConexao();
				
				PreparedStatement psgetserv = conex.prepareStatement("SELECT * FROM tipo_servico;");
				ResultSet rsgetserv = psgetserv.executeQuery();
				ArrayList tservico = new ArrayList();
				String[] service = {};
				while(rsgetserv.next()){
					tservico.add(rsgetserv.getString("descr"));
				}
				if(tservico!=null){
					service = new String[tservico.size()];
					tservico.toArray(service);
				}
				String opcoes = (String) JOptionPane.showInputDialog(null, "Tipos:", "Escolha o serviço", JOptionPane.QUESTION_MESSAGE, null, service, service[0]);
				
				//PAREI AQUI, USAR opcoes PARA PEGAR O cod DE tipo_servico
				
				
				
				String query = "SELECT * FROM tipo_servico WHERE descr = '"+opcoes+"' LIMIT 1";
				PreparedStatement psTipoServ = conex.prepareStatement(query);
				rsTipoServ = psTipoServ.executeQuery();
				if(rsTipoServ.first()){
					String descri = rsTipoServ.getString("descr");
					txtrDescrip.setText(rsTipoServ.getString("descr"));
					lblTipoServ.setText(rsTipoServ.getString("categoria"));
					FieldCodServ.setText(rsTipoServ.getString("cod"));
					psTipoServ.close();
					conex.close();
					rsTipoServ.close();
				} else{
					txtrDescrip.setText("Serviço não encontrado");
					psTipoServ.close();
					conex.close();
				}
				
			} catch (SQLException e) {
				System.out.println("Houve erro");
				e.printStackTrace();
			}
		}
		if(evento.getSource().equals(btnConsultarAnimais)){
			try{
				Connection conex = getConexao();
				String query = "SELECT * FROM animal WHERE cpf_cliente = "+FieldCPF.getText()+";";
				PreparedStatement psGetAni = conex.prepareStatement(query);
				ResultSet rsGetAni = psGetAni.executeQuery();
				while(rsGetAni.next()){
					model.addElement(rsGetAni.getString("nomea"));
				}
				
			} catch (SQLException e) {
				System.out.println("Houve erro");
				e.printStackTrace();
			}
		}
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaFuncionario tela = new TelaFuncionario(funcionario);
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnCriarAgendamento)){
			try{
				Connection conec = getConexao();
				Statement cadastrarST = conec.createStatement();
				cadastrarST.execute("INSERT INTO fatura(stats, vl_total, dt_venc) VALUES("
						+ "'aguardando confirmação de pagamento',"+textPrice.getText()+",DATE_ADD(now() , INTERVAL 7 DAY));");
				ResultSet rscodfat = cadastrarST.executeQuery("SELECT * FROM fatura where stats='aguardando confirmação de pagamento'"
						+ " AND vl_total="+textPrice.getText()+" ORDER BY 'cod' DESC LIMIT 1;");		
				rscodfat.next();
				cadastrarST.execute("INSERT INTO agendamento(tipo_agendamento,hora_inicio,hora_fim,dt_agenda,cod_fatura) VALUES('"
						+ comboTipo.getSelectedItem()+"',STR_TO_DATE('"+textHora.getText()+"', '%k:%i'),STR_TO_DATE('"+
						textHoraF.getText()+"', '%k:%i'),now(),"+rscodfat.getString("cod")+");"); //ERRO NO COD WTF
						
				cadastrarST.execute("INSERT INTO realiza(cpf_cliente,cpf_func,data_marcada) VALUES('"+FieldCPF.getText()+"','"+
						funcionario.getCPF()+"',NOW());"						
						);
				
				Statement busca = conec.createStatement();
				ResultSet rbusca = busca.executeQuery("SELECT * FROM agendamento ORDER BY 'id' DESC LIMIT 1;");
				rbusca.next();
				String idagen = rbusca.getString("id");
				rbusca = busca.executeQuery("SELECT * FROM animal WHERE nomea='"+list.getSelectedValue().toString()+
						"' and cpf_cliente="+FieldCPF.getText()+";");
				rbusca.next();
				String idani = rbusca.getString("id");
				//pegar idagend do agendamento feito e usar para inserir em o envolve
				
				cadastrarST.execute("INSERT INTO envolve(id_animal, id_agend) VALUES("+idani+","+idagen+");");
				if(comboTipo.equals("CLI")){
					
				} else if (comboTipo.equals("PET")){
					
				} else{//LAB
					
				}
				rscodfat.close();
				rbusca.close();
				cadastrarST.close();
				busca.close();
				conec.close();
			}catch (SQLException e) {
				System.out.println("Houve erro no cadastro do agendamento");
				e.printStackTrace();
			}
			JOptionPane.showMessageDialog(null, "Agendamento Realizado");
			dispose();
			TelaFuncionario tela = new TelaFuncionario(funcionario);
			tela.setVisible(true);
		}
	}
}
