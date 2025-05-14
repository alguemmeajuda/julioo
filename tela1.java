
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author suporte
 */
public class tela1 extends javax.swing.JFrame {

    private void calcularMultasEMostrarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tTable.getModel();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                String strVenc = modelo.getValueAt(i, 0).toString();
                String strVal = modelo.getValueAt(i, 1).toString().replace("R$", "").replace(",", ".").trim();
                String strDataAtual = modelo.getValueAt(i, 2).toString();

                if (strVenc.isEmpty() || strVal.isEmpty() || strDataAtual.isEmpty()) {
                    continue;
                }

                LocalDate vencimento = LocalDate.parse(strVenc, formatter);
                LocalDate dataAtual = LocalDate.parse(strDataAtual, formatter);
                double valorDevido = Double.parseDouble(strVal);

                long diasAtraso = ChronoUnit.DAYS.between(vencimento, dataAtual);
                diasAtraso = Math.max(0, diasAtraso);

                double multa = diasAtraso > 0 ? valorDevido * 0.02 : 0.0;
                double juros = diasAtraso > 0 ? valorDevido * 0.00034 * diasAtraso : 0.0;

                double valorReceber = valorDevido + multa + juros;

                modelo.setValueAt(String.format("R$ %.2f", valorDevido), i, 1);
                modelo.setValueAt(diasAtraso, i, 3);
                modelo.setValueAt(String.format("R$ %.2f", multa), i, 4);
                modelo.setValueAt(String.format("R$ %.2f", juros), i, 5);
                modelo.setValueAt(String.format("R$ %.2f", valorReceber), i, 6);

            } catch (Exception e) {
                // ignora erros por dados mal formatados
            }
        }
    }

    /**
     * Creates new form tela1
     */
    public tela1() {
        initComponents();
        configurarTabela(); // Ativa a edição nas colunas corretas
    }

    private void configurarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tTable.getModel();
        int i = modelo.getRowCount();
        while (i-- > 0) {
            modelo.removeRow(i);
        }

    }
    

    private void ExcluirLinha() {
        int row = tTable.getSelectedRow();
        if (row >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) tTable.getModel();
            modelo.removeRow(row);
            calcularMultasEMostrarTabela(); // Recalcula os valores após remoção
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma linha para excluir.");
        }
    }

    private void CalculaColuna() {
        DefaultTableModel modelo = (DefaultTableModel) tTable.getModel();
        double somaDevido = 0.0;
        double somaReceber = 0.0;

        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                String devidoStr = modelo.getValueAt(i, 1).toString().replace("R$", "").replace(",", ".").trim();
                String receberStr = modelo.getValueAt(i, 6).toString().replace("R$", "").replace(",", ".").trim();

                somaDevido += Double.parseDouble(devidoStr);
                somaReceber += Double.parseDouble(receberStr);

            } catch (Exception e) {
                // ignora linhas incompletas
            }
        }

        tValorTotalDevido.setText(String.format("R$ %.2f", somaDevido));
        tReceber.setText(String.format("R$ %.2f", somaReceber));
    }
    private void  parcelinhas(double valorTotal, com.itextpdf.text.Document document, com.itextpdf.text.Font font) throws Exception {
        document.add(new com.itextpdf.text.Paragraph("Simulação de Parcelamento", font));
        document.add(new com.itextpdf.text.Paragraph(" ", font)); // Espaço
        // Pagamento à vista
        document.add(new com.itextpdf.text.Paragraph("Pagamento à vista: R$ " + String.format("%.2f", valorTotal), font));
        document.add(new com.itextpdf.text.Paragraph(" ", font)); // Espaço
        if (valorTotal > 10000) {
            // Parcelamento de 6x a 8x
            for (int i = 6; i <= 8; i++) {
                double valorParcela = valorTotal / i;
                document.add(new com.itextpdf.text.Paragraph(i + "x de R$ " + String.format("%.2f", valorParcela), font));
            }
        } else if (valorTotal > 3000) {
            // Parcelamento de 2x a 4x
            for (int i = 2; i <= 4; i++) {
                double valorParcela = valorTotal / i;
                document.add(new com.itextpdf.text.Paragraph(i + "x de R$ " + String.format("%.2f", valorParcela), font));
            }
        } else {
            document.add(new com.itextpdf.text.Paragraph("Valor total não é suficiente para parcelamento.", font));
        }
        document.add(new com.itextpdf.text.Paragraph(" ", font)); // Espaço
        document.add(new com.itextpdf.text.Paragraph("Relatório de Mensalidades", font));
        document.add(new com.itextpdf.text.Paragraph("Aluno: " + tAluno.getText(), font));
        document.add(new com.itextpdf.text.Paragraph("Curso: " + tCurso.getText(), font));
        document.add(new com.itextpdf.text.Paragraph("Matrícula: " + tMatricula.getText(), font));
        document.add(new com.itextpdf.text.Paragraph("Responsável: " + tResponsavel.getText(), font));
        document.add(new com.itextpdf.text.Paragraph(" ")); // Adiciona um espaço
        // Criação da tabela no PDF
        com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(tTable.getColumnCount());
        // Adiciona os cabeçalhos das colunas da JTable ao PDF
        for (int i = 0; i < tTable.getColumnCount(); i++) {
            pdfTable.addCell(new com.itextpdf.text.Phrase(tTable.getColumnName(i), font));
        }
    }

   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tAluno = new javax.swing.JTextField();
        tCurso = new javax.swing.JTextField();
        tMatricula = new javax.swing.JTextField();
        tResponsavel = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tTable = new javax.swing.JTable();
        tVenci = new javax.swing.JTextField();
        bAdicionar = new javax.swing.JButton();
        tdevido = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        bImprime = new javax.swing.JToggleButton();
        bExclui = new javax.swing.JButton();
        tValorTotalDevido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tReceber = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Calculadora - juros de multa de mensalidades");

        jLabel2.setText("Aluno:");

        jLabel3.setText("Responsável:");

        jLabel4.setText("Curso:");

        jLabel5.setText("Matrícula:");

        tAluno.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        tTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "vencimento", "Valor devido", "Data atual", "Dias em atraso", "Multa", "Atualização Diárias", "Valor a Receber"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tTable);
        if (tTable.getColumnModel().getColumnCount() > 0) {
            tTable.getColumnModel().getColumn(6).setResizable(false);
        }

        tVenci.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tVenciKeyTyped(evt);
            }
        });

        bAdicionar.setText("Adicionar");
        bAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAdicionarActionPerformed(evt);
            }
        });

        tdevido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tdevidoActionPerformed(evt);
            }
        });

        jLabel8.setText("Vencimento");

        jLabel9.setText("Valor Devido");

        bImprime.setText("Salvar em PDF");
        bImprime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bImprimeActionPerformed(evt);
            }
        });

        bExclui.setText("Excluir");
        bExclui.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bExcluiActionPerformed(evt);
            }
        });

        tValorTotalDevido.setEditable(false);
        tValorTotalDevido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tValorTotalDevidoActionPerformed(evt);
            }
        });

        jLabel6.setText("Total devido:");

        tReceber.setEditable(false);

        jLabel7.setText("Total a receber:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel8)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tVenci, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tdevido, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(bAdicionar)
                        .addGap(18, 18, 18)
                        .addComponent(bExclui)
                        .addGap(0, 256, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tAluno)
                            .addComponent(tResponsavel))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(62, 62, 62)
                                .addComponent(jLabel5))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel4)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tCurso, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tMatricula, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(86, 86, 86))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(105, 105, 105)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tValorTotalDevido, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tReceber, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jScrollPane2)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bImprime)
                .addGap(17, 17, 17))
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
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tVenci, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bAdicionar)
                    .addComponent(tdevido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(bExclui))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(tValorTotalDevido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tReceber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(25, 25, 25)
                .addComponent(bImprime)
                .addGap(46, 46, 46))
        );

        pack();
    }// </editor-fold>                        

    private void bAdicionarActionPerformed(java.awt.event.ActionEvent evt) {                                           

    if (isValidDate(tVenci.getText())) {
        String valorStr = tdevido.getText().replace("R$", "").replace(",", ".").trim();
        if (valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe um valor devido válido.");
            return;
        }

        try {
            double valorDevido = Double.parseDouble(valorStr);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataVencimento = LocalDate.parse(tVenci.getText(), dtf);
            LocalDate dataAtual = LocalDate.now();
            long diasAtraso = ChronoUnit.DAYS.between(dataVencimento, dataAtual);
            diasAtraso = Math.max(0, diasAtraso);

            DefaultTableModel modelo = (DefaultTableModel) tTable.getModel();
            modelo.addRow(new Object[]{tVenci.getText(), String.format("R$ %.2f", valorDevido), dtf.format(dataAtual), diasAtraso, "", "", ""});

            tVenci.setText("");
            tdevido.setText("");

            calcularMultasEMostrarTabela();
            CalculaColuna();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido: " + e.getMessage());
        }
    }


    }                                          

    public boolean isValidDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate.parse(dateString, formatter);
            return true;
        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(null, "Erro de data" + e.getMessage());
            return false;
        }
    }


    private void tVenciKeyTyped(java.awt.event.KeyEvent evt) {                                
        char c = evt.getKeyChar();
        String text = tVenci.getText();

        evt.consume();

        if (Character.isDigit(c)) {
            if (text.length() < 2) {

                if (text.length() == 1) {
                    tVenci.setText(text + c);
                } else {
                    tVenci.setText("" + c);
                }
            } else if (text.length() < 5) {

                if (text.length() == 2) {
                    tVenci.setText(text + "/" + c);
                } else {
                    tVenci.setText(text + c);
                }
            } else if (text.length() < 10) {

                if (text.length() == 5) {
                    tVenci.setText(text + "/" + c);
                } else {
                    tVenci.setText(text + c);
                }
            }
        }
    }                               

    private void bImprimeActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // Cria o JFileChooser para o usuário escolher onde salvar
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolha onde salvar o PDF");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Files", "pdf"));

        // Exibe a caixa de diálogo para o usuário escolher o local de salvar
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Verifica se o usuário não adicionou a extensão ".pdf"
            if (!fileToSave.getAbsolutePath().endsWith(".pdf")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
            }

            try {
                // Inicia a criação do documento PDF
                com.itextpdf.text.Document document = new com.itextpdf.text.Document();
                com.itextpdf.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(fileToSave));
                document.open();

                // Fonte para o conteúdo do PDF
                com.itextpdf.text.Font font = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12);

                // Cabeçalho do relatório
                document.add(new com.itextpdf.text.Paragraph("Relatório de Mensalidades", font));
                document.add(new com.itextpdf.text.Paragraph("Aluno: " + tAluno.getText(), font));
                document.add(new com.itextpdf.text.Paragraph("Curso: " + tCurso.getText(), font));
                document.add(new com.itextpdf.text.Paragraph("Matrícula: " + tMatricula.getText(), font));
                document.add(new com.itextpdf.text.Paragraph("Responsável: " + tResponsavel.getText(), font));
                document.add(new com.itextpdf.text.Paragraph(" ")); // Adiciona um espaço

                // Criação da tabela no PDF
                com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(tTable.getColumnCount());

                // Adiciona os cabeçalhos das colunas da JTable ao PDF
                for (int i = 0; i < tTable.getColumnCount(); i++) {
                    pdfTable.addCell(new com.itextpdf.text.Phrase(tTable.getColumnName(i), font));
                }

                // Adiciona os dados das células da JTable ao PDF
                for (int rows = 0; rows < tTable.getRowCount(); rows++) {
                    for (int cols = 0; cols < tTable.getColumnCount(); cols++) {
                        Object value = tTable.getValueAt(rows, cols);
                        pdfTable.addCell(new com.itextpdf.text.Phrase(value != null ? value.toString() : "", font));
                    }
                }
                float[] columnWidths = new float[tTable.getColumnCount()];
                for (int i = 0; i < columnWidths.length; i++) {
                    columnWidths[i] = 2f; // valor maior = coluna mais larga; personalize conforme necessidade
                }

                pdfTable.setWidths(columnWidths);
                pdfTable.setWidthPercentage(100); // A tabela ocupará 100% da largura da página
                pdfTable.setSpacingBefore(10f);
                pdfTable.setSpacingAfter(10f);

                // Adiciona a tabela ao documento
                document.add(pdfTable);

                // ➕ ADICIONA TOTAIS CALCULADOS
                document.add(new com.itextpdf.text.Paragraph("Total Devido: " + tValorTotalDevido.getText(), font));
                document.add(new com.itextpdf.text.Paragraph("Total a Receber (com multas e juros): " + tReceber.getText(), font));
                document.add(new com.itextpdf.text.Paragraph(" ")); // Espaço
                // adiciona a data formatada e colocada no pdf
                LocalDateTime agora = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH: mm");
                String dataFormatada = agora.format(formatter);

                // Cria o parágrafo com a data formatada
                Paragraph paragrafo = new Paragraph("Data de impressão do documento: " + dataFormatada, font);
                document.add(paragrafo);

                //adiciona uma nova pagina(quebra folha); 
                document.newPage();
                
                //adicionar conteudos da segunda pagina...
                double valorTotal = Double.parseDouble(tReceber.getText().replace("R$", "").replace(",", "."));
parcelinhas(valorTotal, document, font);
                // Fecha o documento PDF
                document.close();

                // Exibe mensagem de sucesso
                JOptionPane.showMessageDialog(this, "PDF gerado com sucesso em:\n" + fileToSave.getAbsolutePath());

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao gerar PDF: " + e.getMessage());
            }

            

        
        }

    }                                        

    private void tdevidoActionPerformed(java.awt.event.ActionEvent evt) {                                        
        // TODO add your handling code here:
    }                                       

    private void bExcluiActionPerformed(java.awt.event.ActionEvent evt) {                                        
        ExcluirLinha();
        CalculaColuna();

    }                                       

    private void tValorTotalDevidoActionPerformed(java.awt.event.ActionEvent evt) {                                                  
        // TODO add your handling code here:
    }                                                 

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(tela1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tela1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tela1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tela1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new tela1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton bAdicionar;
    private javax.swing.JButton bExclui;
    private javax.swing.JToggleButton bImprime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField tAluno;
    private javax.swing.JTextField tCurso;
    private javax.swing.JTextField tMatricula;
    private javax.swing.JTextField tReceber;
    private javax.swing.JTextField tResponsavel;
    private javax.swing.JTable tTable;
    private javax.swing.JTextField tValorTotalDevido;
    private javax.swing.JTextField tVenci;
    private javax.swing.JTextField tdevido;
    // End of variables declaration                   
}
