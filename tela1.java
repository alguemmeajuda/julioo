package src;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.swing.table.DefaultTableModel;

public class tela1 extends javax.swing.JFrame {

    public tela1() {
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaValores = new javax.swing.JTable();
        tAluno = new javax.swing.JTextField();
        tCurso = new javax.swing.JTextField();
        tMatricula = new javax.swing.JTextField();
        tResponsavel = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tDevido = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tAtualizado = new javax.swing.JTextField();
        btnCalcular = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Calculadora - juros de multa de mensalidades");

        jLabel2.setText("Aluno:");
        jLabel3.setText("Responsável:");
        jLabel4.setText("Curso:");
        jLabel5.setText("Matrícula:");
        jLabel6.setText("Total devido:");
        jLabel7.setText("Total atualizado:");
        btnCalcular.setText("Calcular");

        btnCalcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcularMultasEMostrarTabela();
            }
        });

        tabelaValores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "Vencimento", "Valor devido", "Data atual", "Dias em atraso", "Multa", "Atualização diária", "Valor a receber"
            }
        ));
        jScrollPane1.setViewportView(tabelaValores);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tDevido, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(105, 105, 105)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tAtualizado, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCalcular)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tAluno)
                    .addComponent(tResponsavel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jLabel5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(tAluno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(tMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tDevido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(tAtualizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCalcular))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }

    private void calcularMultasEMostrarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tabelaValores.getModel();
        modelo.setRowCount(0);

        String[] datasVencimento = {"10/04/2025", "15/04/2025"};
        double[] valoresDevidos = {500.00, 750.00};

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataAtual = LocalDate.now();

        double totalDevido = 0.0;
        double totalAtualizado = 0.0;

        for (int i = 0; i < datasVencimento.length; i++) {
            LocalDate vencimento = LocalDate.parse(datasVencimento[i], formatter);
            double valorDevido = valoresDevidos[i];

            long diasAtraso = ChronoUnit.DAYS.between(vencimento, dataAtual);
            diasAtraso = Math.max(0, diasAtraso);

            double multa = diasAtraso > 0 ? valorDevido * 0.02 : 0.0;
            double juros = diasAtraso > 0 ? valorDevido * 0.00033 * diasAtraso : 0.0;
            double valorReceber = valorDevido + multa + juros;

            modelo.addRow(new Object[]{
                datasVencimento[i],
                String.format("%.2f", valorDevido),
                dataAtual.format(formatter),
                diasAtraso,
                String.format("%.2f", multa),
                String.format("%.2f", juros),
                String.format("%.2f", valorReceber)
            });

            totalDevido += valorDevido;
            totalAtualizado += valorReceber;
        }

        tDevido.setText(String.format("%.2f", totalDevido));
        tAtualizado.setText(String.format("%.2f", totalAtualizado));
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new tela1().setVisible(true);
        });
    }

    private javax.swing.JButton btnCalcular;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelaValores;
    private javax.swing.JTextField tAluno, tAtualizado, tCurso, tDevido, tMatricula, tResponsavel;
}