package modelo;

import excecoes.ItemNaoEncontradoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Inventario {
    private List<Item> itens;

    public Inventario() {
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(Item item) {
        if (item != null) {
            this.itens.add(item);
        }
    }

    public Item buscarItem(String nomeItem) throws ItemNaoEncontradoException {
        if (nomeItem == null) {
            throw new ItemNaoEncontradoException("[!] Nome do item inválido.");
        }

        for (Item item : this.itens) {
            if (item.getNome().equalsIgnoreCase(nomeItem)) {
                return item;
            }
        }

        throw new ItemNaoEncontradoException("[!] Item não encontrado no inventário: " + nomeItem);
    }

    public Item removerItem(String nomeItem) throws ItemNaoEncontradoException {
        Item encontrado = buscarItem(nomeItem);
        this.itens.remove(encontrado);
        return encontrado;
    }

    public void usarItem(String nomeItem, Heroi heroi) throws ItemNaoEncontradoException {
        if (heroi == null) {
            return;
        }

        Item item = removerItem(nomeItem);

        if (item.getTipo() == TipoItem.CURA) {
            heroi.curar(item.getValorEfeito());
        } else if (item.getTipo() == TipoItem.MANA) {
            heroi.recuperarMana(item.getValorEfeito());
        }
    }

    public List<Item> getItens() {
        return Collections.unmodifiableList(this.itens);
    }

    public boolean estaVazio() {
        return this.itens.isEmpty();
    }
}