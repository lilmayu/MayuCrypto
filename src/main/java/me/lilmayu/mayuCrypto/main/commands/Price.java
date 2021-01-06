package me.lilmayu.mayuCrypto.main.commands;

import com.jagrosh.jdautilities.command.CommandEvent;
import me.lilmayu.mayuCrypto.api.kucoin.Kucoin;
import me.lilmayu.mayuCrypto.main.objects.MayuCommand;
import me.lilmayu.mayuCrypto.main.utils.CryptoSymbol;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Code from MurKoin
 */

public class Price extends MayuCommand {

    public Price() {
        this.name = "Price";
        this.aliases = new String[]{"price"};
        this.guildOnly = false;

        Help.addCommand(this);
    }

    @Override
    protected void execute(CommandEvent event) {
        CryptoSymbol cryptoSymbol = new CryptoSymbol(event.getArgs());
        try {
            JSONObject ticker = Kucoin.marketData.getTicker(cryptoSymbol.toString());
            String code = ticker.getString("code");
            if (code.equals("200000")) {
                event.reply("Price for **" + cryptoSymbol.getFirst() + "** is: **" + ticker.getJSONObject("data").getString("price") + " " + cryptoSymbol.getSecond() + "**");
            } else {
                event.reply(ticker.getString("msg"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getDescription() {
        return "Simple price command. Bot will reply with current price of chosen cryptocurrency.";
    }

    @Override
    public String getSyntax() {
        return "[Cryptocurrency symbol]";
    }

    @Override
    public List<String> getExamples() {
        List<String> examples = new ArrayList<>();
        examples.add("BTC-USDT");
        examples.add("ETH-BTC");
        examples.add("BTC");
        return examples;
    }
}
