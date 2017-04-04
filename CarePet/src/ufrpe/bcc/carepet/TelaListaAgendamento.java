package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class TelaListaAgendamento extends JFrame implements ActionListener {

	private JPanel contentPane;
	private Funcionario func;
	private JList listClientes = new JList();
	private JList listAgendamentos = new JList();
	private DefaultListModel modelCl = new DefaultListModel();
	private DefaultListModel modelAg = new DefaultListModel();
	private JButton btnVoltar;
	private JButton btnConfirmarMod;
	private JButton btnDeletar;
	private JButton btnModificar;
	private JButton btnCriarAgendamento;
	private JLabel lblAgendamentos;
	private Connection conex;
	//private JList listAM = new JList();
	//private DefaultListModel modelAM = new DefaultListModel();
	private int altera=0;
	//private int previndex;
	
	
static {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("Problemas carregando o Driver do MySQL");
		}
	}
	
	public static Connection getConexao(Funcionario func) throws SQLException {
		
		Connection retorno = null;
		retorno = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/carepet?autoReconnect=true&useSSL=false", func.getCPF(), func.getSenha()); // nome do esquema, usuário e senha
		return retorno;
	}
	/**
	 * Create the frame.
	 */
	public TelaListaAgendamento(Funcionario funcionario) {
		int ilista1 = 0;
		func = funcionario;
		try{
			conex = getConexao(func);
			conex.setAutoCommit(false);
			String query = "	SELECT * FROM cliente;";
			PreparedStatement psGetCli = conex.prepareStatement(query);
			ResultSet rsGetCli = psGetCli.executeQuery();
			while(rsGetCli.next()){
				modelCl.addElement(rsGetCli.getString("nome"));
				ilista1++;
			}
			psGetCli.close();
			conex.close();
		} catch (SQLException e) {
			System.out.println("Houve erro");
			e.printStackTrace();
		}
		if(ilista1!=0){
			listClientes.setModel(modelCl);
		}
		
		listClientes.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) { //pegar nome->CPF, CPF->realiza, realiza->agendamento
				//ResultSet rsagenid;
				int escolha=5;
				
				modelAg = new DefaultListModel();
				if (!arg0.getValueIsAdjusting()) {
					if(altera==1){
						Object[] options = {"Voltar",
			                    "Descartar"};
						escolha = JOptionPane.showOptionDialog(null, "Você está prestes a descartar as alterações"
								+ "feitas"  ,
							    "Atenção", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
							    null,
							    options,
							    options[1]); //era pra ser 2 no lugar de 1 ?
					}
					if(escolha==1 || altera==0){
						lblAgendamentos.setText("Deletado Modificado                                    Agendamentos ");
						lblAgendamentos.setText(lblAgendamentos.getText()+"de "+listClientes.getSelectedValue().toString());
					altera = 0;
	              int ilista2 = 0;
	              ResultSet rsagend = null;
	              btnModificar.setEnabled(false);
	              btnDeletar.setEnabled(false);
	              try{
	      			Connection conex = getConexao(func);
	      			conex.setAutoCommit(false);
	      			PreparedStatement psagenid = conex.prepareStatement("SELECT id_agend FROM realiza WHERE "
	      					+ "cpf_cliente=(SELECT CPF FROM cliente WHERE nome = '"+listClientes.getSelectedValue().toString()+"');");
	      			ResultSet rsagenid = psagenid.executeQuery();
	      			while(rsagenid.next()){
	      				String query = "SELECT * FROM agendamento WHERE id="+rsagenid.getString("id_agend")+" ;";
	      				Statement s = conex.createStatement();
	      				rsagend = s.executeQuery(query);
	      				while(rsagend.next()){
	      					ilista2++;
	      					String confirm = "";
	      					String faturado = "";
	      					String cancelado = "";
	      					if(rsagend.getInt("confirmado")==1)
	      						confirm = "Sim";
	      					else
	      						confirm = "Não";
	      					if(rsagend.getInt("foi_faturado")==1)  //ALTERAR PARA FATURADO
	      						faturado = "Sim";
	      					else
	      						faturado = "Não";
	      					if(rsagend.getInt("foi_cancelado")==1)
	      						cancelado = "Sim";
	      					else
	      						cancelado = "Não";
	      					modelAg.addElement("D:   M:   ID: "+rsagend.getString("id")+" | Tipo: "
	      							+rsagend.getString("tipo_agendamento")+" | Confirmado: "+confirm+
	      							" | Faturado: "+faturado+" | Cancelado: "+cancelado+" | Data do agendamento: "+rsagend.getString("dt_agenda")+
	      							" | Hora Inicio: "+rsagend.getString("hora_inicio")+
	      							" | Hora fim: "+rsagend.getString("hora_fim")+" | Código de Fatura: "+
	      							rsagend.getString("cod_fatura"));
	      					
	      					
	      				}
	      				
	      			}
	      			if(ilista2!=0)                
	      				listAgendamentos.setModel(modelAg);
	      			if(rsagend != null){
	      				rsagend.close();
	      			} else {
	      				modelAg.removeAllElements();
	      				listAgendamentos.setModel(modelAg);
	      			}
	      			
	      			rsagenid.close();
	      			psagenid.close();
	              
	      			conex.close();
	      			//modelAM = modelAg;
	      			//listAM.setModel(modelAM);	      	
	      		} catch (SQLException e) {
	      			System.out.println("Houve erro");
	      			e.printStackTrace();
	      		}
	              
	            }	}//fechar de if escolha
	        }
		});
		listAgendamentos.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				btnModificar.setEnabled(true);
	              btnDeletar.setEnabled(true);
				
			}
		});
		
		func = funcionario;
		setTitle("Logado como: "+func.getNome());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		btnConfirmarMod = new JButton("Confirmar Modifica\u00E7\u00F5es");
		btnConfirmarMod.setEnabled(false);
		btnConfirmarMod.addActionListener(this);
		
		btnDeletar = new JButton("Deletar");
		btnDeletar.setEnabled(false);
		btnDeletar.addActionListener(this);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.addActionListener(this);
		
		btnCriarAgendamento = new JButton("Criar Agendamento");
		btnCriarAgendamento.addActionListener(this);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 554, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnVoltar)
							.addPreferredGap(ComponentPlacement.RELATED, 243, Short.MAX_VALUE)
							.addComponent(btnCriarAgendamento)
							.addGap(18)
							.addComponent(btnConfirmarMod))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnModificar)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDeletar)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(32)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDeletar)
						.addComponent(btnModificar))
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnVoltar)
						.addComponent(btnConfirmarMod)
						.addComponent(btnCriarAgendamento))
					.addContainerGap())
		);
		
		//listAgendamentos = new JList();
		scrollPane_1.setViewportView(listAgendamentos);
		
		lblAgendamentos = new JLabel("Deletado Modificado                                    Agendamentos ");
		scrollPane_1.setColumnHeaderView(lblAgendamentos);
		
		//listClientes = new JList();
		scrollPane.setViewportView(listClientes);
		
		JLabel lblClientes = new JLabel("Clientes");
		scrollPane.setColumnHeaderView(lblClientes);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnCriarAgendamento)){
			if(func.getTipofunc().equals("Tecnico")){
			dispose();
			TelaAgendamento tela = new TelaAgendamento(func);
			tela.setVisible(true);
			}
			else
				JOptionPane.showMessageDialog(null, "É necessário ser um Funcionario Técnico");
		}
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaFuncionario tela = new TelaFuncionario(func);
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnDeletar)){ // marcar como deletar
			if(listAgendamentos.getSelectedValue()!=null){
			altera=1;
			String newline;
			if(modelAg.getElementAt(listAgendamentos.getSelectedIndex()).toString().contains("D:x")){
				newline ="D: "+ modelAg.getElementAt(listAgendamentos.getSelectedIndex()).toString().substring(3);
			}
			else
				newline ="D:x "+ modelAg.getElementAt(listAgendamentos.getSelectedIndex()).toString().substring(3);
			btnConfirmarMod.setEnabled(true);
			modelAg.set(listAgendamentos.getSelectedIndex(), newline);
			listAgendamentos.setModel(modelAg);
			}
			
		}
		if(evento.getSource().equals(btnModificar)){  //abrir outra janela para modificar
			//modificar: confirmado, faturado, cancelado, data, hora inicio, hora fim	
			if(listAgendamentos.getSelectedValue()!=null)
			if(!modelAg.getElementAt(listAgendamentos.getSelectedIndex()).toString().contains("D:x")){
				altera=1;
				String newline = listAgendamentos.getSelectedValue().toString();
				newline = newline.replaceAll("M: ", "M:x");
				btnConfirmarMod.setEnabled(true);
				int escolha = 10;
				Object[] options = {"Confirmado","Faturado", "Cancelado", "Data", "Hora Início", "Hora Fim"};
				escolha = JOptionPane.showOptionDialog(null, "O que deseja modificar:",
					    "Alterar Agendamento", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					    null,
					    options,
					    options[5]);
				if(escolha==0){
					Object[] simnao = {"Sim","Não"};
					int esc = JOptionPane.showOptionDialog(null, "Confirmado:",
						    "Alterar Agendamento", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						    null,
						    simnao,
						    simnao[1]);
					if(esc==0){
						newline = newline.replace("Confirmado: Não", "Confirmado: Sim");
					} else if(esc==1){
						newline =newline.replace("Confirmado: Sim", "Confirmado: Não");
					}
				}
				if(escolha==1){
					Object[] simnao = {"Sim","Não"};
					int esc = JOptionPane.showOptionDialog(null, "Faturado:",
						    "Alterar Agendamento", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						    null,
						    simnao,
						    simnao[1]);
					if(esc==0){
						newline = newline.replace("Faturado: Não", "Faturado: Sim");
					} else if(esc==1){
						newline =newline.replace("Faturado: Sim", "Faturado: Não");
					}
				}
				if(escolha==2){
					Object[] simnao = {"Sim","Não"};
					int esc = JOptionPane.showOptionDialog(null, "Cancelado:",
						    "Alterar Agendamento", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						    null,
						    simnao,
						    simnao[1]);
					if(esc==0){
						newline = newline.replace("Cancelado: Não", "Cancelado: Sim");
					} else if(esc==1){
						newline =newline.replace("Cancelado: Sim", "Cancelado: Não");
					}
				}
				if(escolha==3){
					String data = listAgendamentos.getSelectedValue().toString().split("Data do agendamento: " )[1].split(" ")[0];
					String ndata = data;
					ndata = (String)JOptionPane.showInputDialog(
		                    null,
		                    "Data do agendamento:",
		                    "Alterar Agendamento",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    data);
					if(ndata==null)
						ndata=data;
					newline = newline.replace(data, ndata);
				}
				if(escolha==4){
					String hini = listAgendamentos.getSelectedValue().toString().split("Hora Inicio: " )[1].split(" ")[0];
					String nhini;
					nhini = (String)JOptionPane.showInputDialog(
		                    null,
		                    "Hora de início do agendamento:",
		                    "Alterar Agendamento",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    hini);
					if(nhini==null)
						nhini=hini;
					newline = newline.replace(hini, nhini);
				}
				if(escolha==5){
					String hfim = listAgendamentos.getSelectedValue().toString().split("Hora fim: " )[1].split(" ")[0];
					String nhfim;
					nhfim = (String)JOptionPane.showInputDialog(
		                    null,
		                    "Hora final do agendamento:",
		                    "Alterar Agendamento",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null,
		                    null,
		                    hfim);
					if(nhfim==null)
						nhfim=hfim;
					newline = newline.replace(hfim, nhfim);
				}
				modelAg.set(listAgendamentos.getSelectedIndex(), newline);
				listAgendamentos.setModel(modelAg);
			}
		}
		if(evento.getSource().equals(btnConfirmarMod)){ //checar por D:x e depois M:x em cada cell da lista
			try{
				conex = getConexao(func);
				conex.setAutoCommit(false);
			for(int i=0;i<modelAg.size();i++){
				if(modelAg.get(i).toString().contains("D:x")){ //deletar agendamento
					String idagen = modelAg.get(i).toString().split("ID: ")[1].split(" ")[0];
					String query = "DELETE FROM agendamento WHERE id="+idagen+" ;";
					PreparedStatement psdel = conex.prepareStatement(query);
					psdel.execute();
					psdel.close();
				}
				else if(modelAg.get(i).toString().contains("M:x")){ //atualizar agendamento
					String data = modelAg.get(i).toString().split("Data do agendamento: " )[1].split(" ")[0];
					String hini = modelAg.get(i).toString().split("Hora Inicio: " )[1].split(" ")[0];
					String hfim = modelAg.get(i).toString().split("Hora fim: " )[1].split(" ")[0];
					String idagen = modelAg.get(i).toString().split("ID: ")[1].split(" ")[0];
					int conf=0;
					int fatu=0;
					int canc=0;
					if(modelAg.get(i).toString().contains("Confirmado: Não")){
						conf=0;
					} else if(modelAg.get(i).toString().contains("Confirmado: Sim")){
						conf=1;
					}
					if(modelAg.get(i).toString().contains("Faturado: Não")){
						fatu=0;
					} else if(modelAg.get(i).toString().contains("Faturado: Sim")){
						fatu=1;
					}
					if(modelAg.get(i).toString().contains("Cancelado: Não")){
						canc=0;
					} else if(modelAg.get(i).toString().contains("Cancelado: Sim")){
						canc=1;
					}
					String query = "UPDATE agendamento SET confirmado='"+conf+"',foi_cancelado='"+canc+"', "
							+ "foi_faturado='"+fatu+"',"
							+ " hora_fim='"+hfim+"', hora_inicio='"+hini+"', dt_agenda='"+data+"' "
							+ "WHERE id="+idagen+";";
					PreparedStatement psdel = conex.prepareStatement(query);
					psdel.execute();
					psdel.close();
				}
			}
			conex.commit();
			
		} catch (SQLException e) {
			try{
				conex.rollback();
			} catch (SQLException l) {
				l.printStackTrace();
			}
  			System.out.println("Houve erro ao confirmar modificações");
  			e.printStackTrace();
  		}
			modelAg.removeAllElements();
			listAgendamentos.setModel(modelAg);
			JOptionPane.showMessageDialog(null, "Modificações Realizadas");
			try{
				conex.close();
			} catch (SQLException cl) {
				cl.printStackTrace();
			}
		} 
	} 
}
