package modelo;

public enum Elemento {
    PYRO, HYDRO, ANEMO, ELECTRO;

    public double getMultiplicadorContra(Elemento defensor) {
        if (defensor == null) {
            return 1.0;
        }
        if (this == defensor) {
            return 0.65; // Resistência do mesmo elemento
        }

        switch (this) {
            case HYDRO:
                if (defensor == PYRO) return 1.6; // Vaporização
                if (defensor == ELECTRO) return 1.3;
                break;
            case PYRO:
                if (defensor == ANEMO) return 1.5; // Propagação
                if (defensor == HYDRO) return 0.8; // Apagado
                if (defensor == ELECTRO) return 1.4; // Sobrecarga
                break;
            case ELECTRO:
                if (defensor == HYDRO) return 1.6; // Eletrocussão
                if (defensor == PYRO) return 1.3;
                break;
            case ANEMO:
                return 1.35; // Dispersão sobre qualquer elemento diferente
        }
        return 1.0;
    }

    public String getDescricaoReacao(Elemento defensor) {
        if (defensor == null) {
            return "";
        }
        if (this == defensor) {
            return " [RESISTÊNCIA ELEMENTAL: -35%]";
        }

        switch (this) {
            case HYDRO:
                if (defensor == PYRO) return " [VAPORIZAÇÃO! Dano x1.6]";
                if (defensor == ELECTRO) return " [CONDUÇÃO ELÉTRICA! Dano x1.3]";
                break;
            case PYRO:
                if (defensor == ANEMO) return " [PROPAGAÇÃO DE CHAMAS! Dano x1.5]";
                if (defensor == HYDRO) return " [CHAMAS APAGADAS! Dano x0.8]";
                if (defensor == ELECTRO) return " [SOBRECARGA EXPLOSIVA! Dano x1.4]";
                break;
            case ELECTRO:
                if (defensor == HYDRO) return " [ELETROCUSSÃO INTENSA! Dano x1.6]";
                if (defensor == PYRO) return " [SOBRECARGA! Dano x1.3]";
                break;
            case ANEMO:
                return " [DISPERSÃO ELEMENTAL! Dano x1.35]";
        }
        return "";
    }
}
