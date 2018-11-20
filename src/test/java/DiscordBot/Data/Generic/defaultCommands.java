package DiscordBot.Data.Generic;

import DiscordBot.Main;
import ideaeclipse.DiscordAPI.terminal.CustomTerminal;
import ideaeclipse.DiscordAPI.terminal.Terminal;
import ideaeclipse.DiscordAPI.utils.DiscordUtils;
import ideaeclipse.latexConverter.LatexConverter;

import java.io.IOException;

public class defaultCommands extends CustomTerminal {
    private Terminal t;

    public defaultCommands(Terminal t) {
        this.t = t;
    }

    public String whoAmI() {
        return "You are: " + t.getUser();
    }

    public String getCurrentFunction() {
        if (t.getCurrentFunction() != null)
            return "Your current function is: " + t.getCurrentFunction();
        else
            return "You're not currently attached to a function";
    }

    public String showString(String s) {
        return "This is your string: " + s;
    }

    public void sendImage() {
        DiscordUtils.Search.CHANNEL(Main.bot.getChannels(), "bot").sendMessage("Test", "wallpaper.jpg");
    }

    public String equation(String eq) {
        if (!eq.equals("help")) {
            LatexConverter converter = new LatexConverter("latexTemp", System.getProperty("os.name").toLowerCase().contains("windows")?"C:\\Program Files\\MikTex 2.9\\miktex\\bin\\x64\\pdflatex.exe":"pdflatex");
            eq = eq.replaceAll("\\\\\\\\", "\\\\");
            try {
                DiscordUtils.Search.CHANNEL(Main.bot.getChannels(), "bot").sendMessage("Test", converter.convert(eq));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            StringBuilder info = new StringBuilder("```Latex Commands Info:\n");
            info.append("Math Constructs:\n");
            info.append("   -> Fractions: \\\\frac{numerator}{denominator}").append("\n");
            info.append("   -> Primed: f'").append("\n");
            info.append("   -> Square root (base 2): \\\\sqrt{under symbol}").append("\n");
            info.append("   -> Square root (base n): \\\\sqrt[n]{under symbol}").append("\n");
            info.append("   -> OverLine: \\\\overline{underline}").append("\n");
            info.append("Delimiters:\n");
            info.append("   -> Bar: |").append("\n");
            info.append("   -> Curly brace open: \\\\{").append("\n");
            info.append("   -> Curly brace close: \\\\}").append("\n");
            info.append("   -> Floor left: \\\\lfloor").append("\n");
            info.append("   -> Floor right: \\\\rfloor").append("\n");
            info.append("   -> Ceiling left: \\\\lceil").append("\n");
            info.append("   -> Ceiling right: \\\\rceil").append("\n");
            info.append("Variable Size symbols:\n");
            info.append("Add \\\\limits_below^above to added digits above and below to each variable symbol\n");
            info.append("   -> Summation: \\\\sum").append("\n");
            info.append("   -> Integral: \\\\int").append("\n");
            info.append("   -> Intersection: \\\\bigcap").append("\n");
            info.append("   -> Union: \\\\bigcup").append("\n");
            info.append("   -> Or: \\\\bigvee").append("\n");
            info.append("   -> And: \\\\bigwedge").append("\n");
            info.append("StandardFunction:\n");
            info.append("Add $anyFunction_{subscript}{function value}}").append("\n");
            info.append("   -> tan: \\\\tan").append("\n");
            info.append("   -> cos: \\\\cos").append("\n");
            info.append("   -> sin: \\\\sin").append("\n");
            info.append("   -> cot: \\\\cot").append("\n");
            info.append("   -> sec: \\\\sec").append("\n");
            info.append("   -> csc: \\\\csc").append("\n");
            info.append("   -> log: \\\\log").append("\n");
            info.append("Limits:\n");
            info.append("   -> Limit example: \\\\lim_{x \\\\to $} f(x)").append("\n");
            info.append("   -> Infinity: \\\\infty").append("\n");
            info.append("Binary operators / Relational Symbols:\n");
            info.append("   -> Add: +").append("\n");
            info.append("   -> Subtract: -").append("\n");
            info.append("   -> Multiplication: \\\\times || \\\\centerdot").append("\n");
            info.append("   -> Division: \\\\div || see fraction above").append("\n");
            info.append("   -> Intersection: \\\\cap").append("\n");
            info.append("   -> Union: \\\\cup").append("\n");
            info.append("   -> Or: \\\\vee").append("\n");
            info.append("   -> And: \\\\wedge").append("\n");
            info.append("   -> Implies: \\\\rightarrow").append("\n");
            info.append("   -> Perpendicular: \\\\perp").append("\n");
            info.append("   -> Equivalent: \\\\equiv").append("\n");
            info.append("   -> Approximation: \\\\approxeq").append("\n");
            info.append("   -> Therefore: \\\\therefore").append("\n");
            info.append("   -> Greater than: >").append("\n");
            info.append("   -> Greater than or equals: \\\\geq").append("\n");
            info.append("   -> Less than:  <").append("\n");
            info.append("   -> Less than or equals: \\\\leq").append("\n");
            info.append("Discrete Math symbols:\n");
            info.append("   -> For all: \\\\forall").append("\n");
            info.append("   -> Some: \\\\exists").append("\n");
            info.append("   -> E (in): \\\\in").append("\n");
            info.append("   -> E/ (not in): \\\\notin").append("\n");
            info.append("   -> Subset: \\\\subset").append("\n");
            //info.append("   ->").append("\n");
            info.append("```");
            return info.toString();
        }
        return null;
    }

    @Override
    public void done() {

    }
}
