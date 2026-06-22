import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import java.util.*;
public class Sudoku extends JFrame{

    private JTextField[][] cells = new JTextField[9][9];

    public Sudoku() {

        setTitle("Sudoku Solver");
        setSize(700, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(252, 248, 255));

        JLabel title = new JLabel("Sudoku Solver", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(new Color(109, 40, 217));
        add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(9, 9));
        gridPanel.setBackground(new Color(252, 248, 255));

        Font cellFont = new Font("Segoe UI", Font.BOLD, 22);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {

                JTextField field = new JTextField();

                field.setHorizontalAlignment(JTextField.CENTER);
                field.setFont(cellFont);

                if ((row / 3 + col / 3) % 2 == 0) {
                    field.setBackground(Color.WHITE);
                }
                else {
                    field.setBackground(new Color(245, 236, 255));
                }

                field.setBorder(
                        new LineBorder(
                                new Color(216, 180, 224),1 )
                );

                cells[row][col] = field;
                gridPanel.add(field);
            }
        }

        add(gridPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(252, 248, 255));

        JButton solveButton = new JButton("Solve Sudoku");
        JButton resetButton = new JButton("Reset");
        JButton sampleButton = new JButton("Load Sample");

        solveButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        resetButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sampleButton.setFont(new Font("Segoe UI", Font.BOLD, 16));

        solveButton.setBackground(new Color(167, 139, 250));
        resetButton.setBackground(new Color(251, 146, 60));
        sampleButton.setBackground(new Color(244, 114, 182));

        solveButton.setForeground(Color.WHITE);
        resetButton.setForeground(Color.WHITE);
        sampleButton.setForeground(Color.WHITE);

        solveButton.setFocusPainted(false);
        resetButton.setFocusPainted(false);
        sampleButton.setFocusPainted(false);

        solveButton.setBorderPainted(false);
        resetButton.setBorderPainted(false);
        sampleButton.setBorderPainted(false);

        buttonPanel.add(sampleButton);
        buttonPanel.add(solveButton);
        buttonPanel.add(resetButton);

        add(buttonPanel, BorderLayout.SOUTH);

        sampleButton.addActionListener(e -> loadSamplePuzzle());

        resetButton.addActionListener(e -> clearBoard());

        solveButton.addActionListener(e -> solvePuzzle());

        setVisible(true);
    }

    private void loadSamplePuzzle() {

        int[][] sample = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                if (sample[i][j] == 0)
                    cells[i][j].setText("");
                else
                    cells[i][j].setText(String.valueOf(sample[i][j]));
                    cells[i][j].setForeground(new Color(88, 28, 135));
            }
        }
    }

    private void clearBoard() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                cells[i][j].setText("");
                if((i / 3 + j / 3) % 2 == 0) {
                    cells[i][j].setBackground(Color.WHITE);
                }else{
                    cells[i][j].setBackground(new Color(245, 236, 255));
                }
                cells[i][j].setForeground(Color.BLACK);
            }
        }
    }

    private void solvePuzzle() {

        int[][] board = new int[9][9];

        try {

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {

                    String text = cells[i][j].getText().trim();

                    if (text.isEmpty()) {
                        board[i][j] = 0;
                    } else {

                        int value = Integer.parseInt(text);

                        if (value < 1 || value > 9) {
                            throw new NumberFormatException();
                        }

                        board[i][j] = value;
                    }
                }
            }

            if (solveSudoku(board)) {

                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {

                        cells[i][j].setText(String.valueOf(board[i][j]));
                        cells[i][j].setBackground(new Color(243, 232, 255));
                        cells[i][j].setForeground(new Color(126, 34, 206));
                    }
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Sudoku Solved Successfully!"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "No Solution Exists!"
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter numbers from 1-9 only."
            );
        }
    }

    private boolean solveSudoku(int[][] board) {

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                if (board[row][col] == 0) {

                    for (int num = 1; num <= 9; num++) {

                        if (isSafe(board, row, col, num)) {

                            board[row][col] = num;

                            if (solveSudoku(board))
                                return true;

                            board[row][col] = 0;
                        }
                    }

                    return false;
                }
            }
        }

        return true;
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {

        for (int x = 0; x < 9; x++) {

            if (board[row][x] == num)
                return false;
        }

        for (int x = 0; x < 9; x++) {

            if (board[x][col] == num)
                return false;
        }

        int startRow = row - row % 3;
        int startCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (board[startRow + i][startCol + j] == num)
                    return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new Sudoku());
    }
}