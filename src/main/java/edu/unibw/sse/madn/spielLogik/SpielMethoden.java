package edu.unibw.sse.madn.spielLogik;

import java.util.Arrays;

import static edu.unibw.sse.madn.spielLogik.FeldBesetztStatus.*;

public class SpielMethoden {
    static FeldBesetztStatus[] feldFuellen(int spielerAnz) {
        FeldBesetztStatus[] board = new FeldBesetztStatus[72];
        Arrays.fill(board, 0, 72, FELD_LEER);
        if (spielerAnz == 2) {
            Arrays.fill(board, 0, 4, FELD_SPIELER1);
            Arrays.fill(board, 8, 12, FELD_SPIELER3);
        } else {
            Arrays.fill(board, 0, 4, FELD_SPIELER1);
            Arrays.fill(board, 4, 8, FELD_SPIELER2);
            Arrays.fill(board, 8, 12, FELD_SPIELER3);
            if (spielerAnz == 4) Arrays.fill(board, 12, 16, FELD_SPIELER4);
        }
        return board;
    }

    /**
     * überprüft, ob der Zug gesetzt werden durfte, unabhängig von einem eventuellen Prio Zug
     */
    public static boolean checkMoveValid(FeldBesetztStatus[] board, FeldBesetztStatus player, int from, int to, int dice) {
        // richtige Spielfigur/nicht selber schlagen
        if (board[from] != player || board[to] == player) return false;

        boolean b = ((from - 32 + dice) % 40 + 32) == to;

        // Spielzug auf Weg
        if (from >= 32 && to >= 32 && b && board[to] != player) {
            // nicht über Start beachten
            int i = from - 32;
            if (player == FELD_SPIELER1 && !((i + dice) >= 40)) return true;
            if (player == FELD_SPIELER2 && !((i + dice) >= 10 && i < 10)) return true;
            if (player == FELD_SPIELER3 && !((i + dice) >= 20 && i < 20)) return true;
            if (player == FELD_SPIELER4 && !((i + dice) >= 30 && i < 30)) return true;
        }
        // raus rücken
        if (dice == 6) {
            //noinspection ConstantConditions
            if (player == FELD_SPIELER1 && from >= 0 && from < 4 && to == 32 && board[to] != FELD_SPIELER1) return true;
            if (player == FELD_SPIELER2 && from >= 4 && from < 8 && to == 42 && board[to] != FELD_SPIELER2) return true;
            if (player == FELD_SPIELER3 && from >= 8 && from < 12 && to == 52 && board[to] != FELD_SPIELER3)
                return true;
            if (player == FELD_SPIELER4 && from >= 12 && from < 16 && to == 62 && board[to] != FELD_SPIELER4)
                return true;
        }
        // rein rücken
        if (player == FELD_SPIELER1 && from >= 66 && from <= 71 && to >= 16 && to <= 19 && from + dice == to + 56) {
            for (int i = 16; i < to; i++) if (board[i] == FELD_SPIELER1) return false;
            return true;
        }
        if (player == FELD_SPIELER2 && from >= 36 && from <= 41 && to >= 20 && to <= 23 && from + dice == to + 22) {
            for (int i = 20; i < to; i++) if (board[i] == FELD_SPIELER2) return false;
            return true;
        }
        if (player == FELD_SPIELER3 && from >= 46 && from <= 51 && to >= 24 && to <= 27 && from + dice == to + 28) {
            for (int i = 24; i < to; i++) if (board[i] == FELD_SPIELER3) return false;
            return true;
        }
        if (player == FELD_SPIELER4 && from >= 56 && from <= 61 && to >= 28 && to <= 31 && from + dice == to + 34) {
            for (int i = 28; i < to; i++) if (board[i] == FELD_SPIELER4) return false;
            return true;
        }
        // in Zielfeldern rücken
        if (player == FELD_SPIELER1 && from >= 16 && from <= 19 && to >= 17 && to <= 19 && from + dice == to) {
            for (int i = from + 1; i < to; i++) if (board[i] == FELD_SPIELER1) return false;
            return true;
        }
        if (player == FELD_SPIELER2 && from >= 20 && from <= 23 && to >= 21 && to <= 23 && from + dice == to) {
            for (int i = from + 1; i < to; i++) if (board[i] == FELD_SPIELER2) return false;
            return true;
        }
        if (player == FELD_SPIELER3 && from >= 24 && from <= 27 && to >= 25 && to <= 27 && from + dice == to) {
            for (int i = from + 1; i < to; i++) if (board[i] == FELD_SPIELER3) return false;
            return true;
        }
        if (player == FELD_SPIELER4 && from >= 28 && from <= 31 && to >= 29 && to <= 31 && from + dice == to) {
            for (int i = from + 1; i < to; i++) if (board[i] == FELD_SPIELER4) return false;
            return true;
        }
        return false;
    }

    /**
     * @return null, falls kein Prio Move vorhanden, sonst [from, to]
     */
    public static int[] checkForPrioMove(FeldBesetztStatus[] board, FeldBesetztStatus player, int dice) {
        // rausrücken
        if (dice == 6) {
            if (player == FELD_SPIELER1) for (int i = 0; i < 4; i++)
                if (board[i] != FELD_LEER && board[32] != FELD_SPIELER1) return new int[]{i, 32};
            if (player == FELD_SPIELER2) for (int i = 4; i < 8; i++)
                if (board[i] != FELD_LEER && board[42] != FELD_SPIELER2) return new int[]{i, 42};
            if (player == FELD_SPIELER3) for (int i = 8; i < 12; i++)
                if (board[i] != FELD_LEER && board[52] != FELD_SPIELER3) return new int[]{i, 52};
            if (player == FELD_SPIELER4) for (int i = 12; i < 16; i++)
                if (board[i] != FELD_LEER && board[62] != FELD_SPIELER4) return new int[]{i, 62};
        }
        // abrücken
        if (player == FELD_SPIELER1 && board[32] == FELD_SPIELER1 && board[32 + dice] != FELD_SPIELER1 && (board[0] == FELD_SPIELER1 || board[1] == FELD_SPIELER1 || board[2] == FELD_SPIELER1 || board[3] == FELD_SPIELER1))
            return new int[]{32, 32 + dice};
        if (player == FELD_SPIELER2 && board[42] == FELD_SPIELER2 && board[42 + dice] != FELD_SPIELER2 && (board[4] == FELD_SPIELER2 || board[5] == FELD_SPIELER2 || board[6] == FELD_SPIELER2 || board[7] == FELD_SPIELER2))
            return new int[]{42, 42 + dice};
        if (player == FELD_SPIELER3 && board[52] == FELD_SPIELER3 && board[52 + dice] != FELD_SPIELER3 && (board[8] == FELD_SPIELER3 || board[9] == FELD_SPIELER3 || board[10] == FELD_SPIELER3 || board[11] == FELD_SPIELER3))
            return new int[]{52, 52 + dice};
        if (player == FELD_SPIELER4 && board[62] == FELD_SPIELER4 && board[62 + dice] != FELD_SPIELER4 && (board[12] == FELD_SPIELER4 || board[13] == FELD_SPIELER4 || board[14] == FELD_SPIELER4 || board[15] == FELD_SPIELER4))
            return new int[]{62, 62 + dice};
        // schlagen
        for (int i = 39; i >= 0; i--) {
            if (board[i + 32] == player && board[(i + dice) % 40 + 32] != player && board[(i + dice) % 40 + 32] != FELD_LEER) {
                // nicht über Start beachten
                if (player == FELD_SPIELER1 && (i + dice) >= 40) continue;
                if (player == FELD_SPIELER2 && (i + dice) >= 10 && i < 10) continue;
                if (player == FELD_SPIELER3 && (i + dice) >= 20 && i < 20) continue;
                if (player == FELD_SPIELER4 && (i + dice) >= 30 && i < 30) continue;
                return new int[]{i + 32, (i + dice) % 40 + 32};
            }
        }
        return null;
    }

    /**
     * @return [-1, -1] falls kein Zug möglich, sonst [from, to] berücksichtigt Prio
     */
    public static int[] getValidMove(FeldBesetztStatus[] boardState, FeldBesetztStatus field, int wurf) {
        int[] prio = checkForPrioMove(boardState, field, wurf);
        if (prio != null) return prio;
        // innerhalb Zielfelder
        if (field == FELD_SPIELER1) {
            if (checkMoveValid(boardState, field, 16, 16 + wurf, wurf)) return new int[]{16, 16 + wurf};
            if (checkMoveValid(boardState, field, 17, 17 + wurf, wurf)) return new int[]{17, 17 + wurf};
            if (checkMoveValid(boardState, field, 18, 18 + wurf, wurf)) return new int[]{18, 18 + wurf};
        }
        if (field == FELD_SPIELER2) {
            if (checkMoveValid(boardState, field, 20, 20 + wurf, wurf)) return new int[]{20, 20 + wurf};
            if (checkMoveValid(boardState, field, 21, 21 + wurf, wurf)) return new int[]{21, 21 + wurf};
            if (checkMoveValid(boardState, field, 22, 22 + wurf, wurf)) return new int[]{22, 22 + wurf};
        }
        if (field == FELD_SPIELER3) {
            if (checkMoveValid(boardState, field, 24, 24 + wurf, wurf)) return new int[]{24, 24 + wurf};
            if (checkMoveValid(boardState, field, 25, 25 + wurf, wurf)) return new int[]{25, 25 + wurf};
            if (checkMoveValid(boardState, field, 26, 26 + wurf, wurf)) return new int[]{26, 26 + wurf};
        }
        if (field == FELD_SPIELER4) {
            if (checkMoveValid(boardState, field, 28, 28 + wurf, wurf)) return new int[]{28, 28 + wurf};
            if (checkMoveValid(boardState, field, 29, 29 + wurf, wurf)) return new int[]{29, 29 + wurf};
            if (checkMoveValid(boardState, field, 30, 30 + wurf, wurf)) return new int[]{30, 30 + wurf};
        }
        // in Zielfelder
        if (field == FELD_SPIELER1) for (int i = 71; i > 71 - wurf; i--)
            if (boardState[i] == FELD_SPIELER1 && i + wurf < 76)
                if (checkMoveValid(boardState, field, i, i + wurf - 56, wurf)) return new int[]{i, i + wurf - 56};
        if (field == FELD_SPIELER2) for (int i = 41; i > 41 - wurf; i--)
            if (boardState[i] == FELD_SPIELER2 && i + wurf < 46)
                if (checkMoveValid(boardState, field, i, i + wurf - 22, wurf)) return new int[]{i, i + wurf - 22};
        if (field == FELD_SPIELER3) for (int i = 51; i > 51 - wurf; i--)
            if (boardState[i] == FELD_SPIELER3 && i + wurf < 56)
                if (checkMoveValid(boardState, field, i, i + wurf - 28, wurf)) return new int[]{i, i + wurf - 28};
        if (field == FELD_SPIELER4) for (int i = 61; i > 61 - wurf; i--)
            if (boardState[i] == FELD_SPIELER4 && i + wurf < 66)
                if (checkMoveValid(boardState, field, i, i + wurf - 34, wurf)) return new int[]{i, i + wurf - 34};
        // sonst zufällig
        int[] start = new int[4];
        int count = 0;
        for (int i = 32; i < 72; i++) {
            if (boardState[i] == field) {
                if (checkMoveValid(boardState, field, i, (i - 32 + wurf) % 40 + 32, wurf)) start[count++] = i;
            }
        }
        if (count > 0) {
            int auswahl = start[(int) (Math.random() * count)];
            return new int[]{auswahl, (auswahl - 32 + wurf) % 40 + 32};
        }

        return new int[]{-1, -1};
    }
}
