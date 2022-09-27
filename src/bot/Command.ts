import {ChatInputApplicationCommandData, Client, CommandInteraction, SlashCommandBuilder} from "discord.js";

export interface Command extends ChatInputApplicationCommandData {
    run: (client: Client, interaction: CommandInteraction) => void;
    applyOptions: (builder: SlashCommandBuilder) => void;
}