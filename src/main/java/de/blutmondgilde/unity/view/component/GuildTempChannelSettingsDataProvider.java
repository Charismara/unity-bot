package de.blutmondgilde.unity.view.component;

import com.vaadin.flow.component.crud.CrudFilter;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import de.blutmondgilde.unity.data.jpa.guild.GuildTempChannelSettings;
import de.blutmondgilde.unity.data.jpa.guild.GuildTempChannelSettingsRepository;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Slf4j
public class GuildTempChannelSettingsDataProvider extends AbstractBackEndDataProvider<GuildTempChannelSettings, CrudFilter> {
    private final GuildTempChannelSettingsRepository tempChannelSettingsRepository;
    private final long guildId;

    public GuildTempChannelSettingsDataProvider(GuildTempChannelSettingsRepository tempChannelSettingsRepository, long guildId) {
        this.tempChannelSettingsRepository = tempChannelSettingsRepository;
        this.guildId = guildId;
    }


    @Override
    protected Stream<GuildTempChannelSettings> fetchFromBackEnd(Query<GuildTempChannelSettings, CrudFilter> query) {
        int offset = query.getOffset();
        int limit = query.getLimit();

        Stream<GuildTempChannelSettings> stream = tempChannelSettingsRepository.findByGuildId(this.guildId).stream();
        log.info("Found {} entries", stream.count());

        if (query.getFilter().isPresent()) {
            stream = stream
                .filter(predicate(query.getFilter().get()))
                .sorted(comparator(query.getFilter().get()));
        }

        return stream.skip(offset).limit(limit);
    }

    @Override
    protected int sizeInBackEnd(Query<GuildTempChannelSettings, CrudFilter> query) {
        return tempChannelSettingsRepository.findByGuildId(this.guildId).size();
    }

    private static Predicate<GuildTempChannelSettings> predicate(CrudFilter filter) {
        // For RDBMS just generate a WHERE clause
        return filter.getConstraints().entrySet().stream()
            .map(constraint -> (Predicate<GuildTempChannelSettings>) person -> {
                try {
                    Object value = valueOf(constraint.getKey(), person);
                    return value != null && value.toString().toLowerCase()
                        .contains(constraint.getValue().toLowerCase());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            })
            .reduce(Predicate::and)
            .orElse(e -> true);
    }

    private static Comparator<GuildTempChannelSettings> comparator(CrudFilter filter) {
        return filter.getSortOrders().entrySet().stream()
            .map(sortClause -> {
                try {
                    Comparator<GuildTempChannelSettings> comparator = Comparator.comparing(settings ->
                        (Comparable) valueOf(sortClause.getKey(), settings)
                    );

                    if (sortClause.getValue() == SortDirection.DESCENDING) {
                        comparator = comparator.reversed();
                    }

                    return comparator;

                } catch (Exception ex) {
                    return (Comparator<GuildTempChannelSettings>) (o1, o2) -> 0;
                }
            })
            .reduce(Comparator::thenComparing)
            .orElse((o1, o2) -> 0);
    }

    private static Object valueOf(String fieldName, GuildTempChannelSettings person) {
        try {
            Field field = GuildTempChannelSettings.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(person);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
