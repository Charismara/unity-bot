import {Command} from "./Command";
import {number} from "prop-types";

const Random: Command = {
    name: "random",
    description: "Returns a random value from a given list",
    run: async (client, interaction) => {
        const list: string = interaction.options.getString('options');
        const options = list.split(';');
        const random = Math.random() * (options.length - 1) + 1
        const result = options[Math.floor(random)];
        await interaction.editReply({
            content: result
        });
    },
    applyOptions: builder => {
        builder.addStringOption(builder1 => builder1.setName('options')
            .setDescription('List of options. Seperated with a ;')
            .setRequired(true)
        )
    }
}

export const Commands: Command[] = [
    Random
];