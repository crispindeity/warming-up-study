package study.day04.fruit.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import study.day04.fruit.domain.Fruit;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class FruitJdbcRepository implements FruitRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public FruitJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("fruit")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name", "warehousingDate", "price", "status");
    }

    @Override
    public Fruit save(Fruit fruit) {
        BeanPropertySqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(fruit) {
            @Override
            public Object getValue(String paramName) throws IllegalArgumentException {
                Object value = super.getValue(paramName);
                if (value instanceof Enum) {
                    return value.toString();
                }
                return value;
            }
        };
        Number key = jdbcInsert.executeAndReturnKey(sqlParameterSource);
        return Fruit.of(key.longValue(), fruit.name(), fruit.warehousingDate(), fruit.price());
    }

    @Override
    public Long update(Long id) {
        String sql = "UPDATE fruit SET fruit.status = :status WHERE id = :id";
        Fruit fruit = findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 과일이 없습니다."));

        Fruit updatedFruit = Fruit.updateStat(fruit);

        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name", updatedFruit.name())
                .addValue("warehousingDate", updatedFruit.warehousingDate())
                .addValue("price", updatedFruit.price())
                .addValue("status", updatedFruit.status().name())
                .addValue("id", id);

        jdbcTemplate.update(sql, parameterSource);
        return id;
    }

    @Override
    public Optional<Long> findSoldsAmountByFruitName(String name) {
        String sql = "SELECT sum(price) as salesAmount FROM fruit where name = :name group by status having status = 'SOLD'";
        List<Long> price = jdbcTemplate.query(sql, new MapSqlParameterSource("name", name),
                (rs, rowNum) -> rs.getLong("salesAmount"));
        return price.stream().reduce(Long::sum);
    }

    @Override
    public Optional<Long> findSalesAmountByFruitName(String name) {
        String sql = "SELECT sum(price) as salesAmount FROM fruit where name = :name group by status having status = 'SALE'";
        List<Long> price = jdbcTemplate.query(sql, new MapSqlParameterSource("name", name),
                (rs, rowNum) -> rs.getLong("salesAmount"));
        return price.stream().reduce(Long::sum);
    }

    @Override
    public Optional<Fruit> findById(Long id) {
        String sql = "SELECT id, name, warehousingDate, price, status FROM fruit WHERE id = :id";
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id),
                            (rs, rowNum) -> Fruit.of(
                                    rs.getLong("id"),
                                    rs.getString("name"),
                                    rs.getDate("warehousingDate").toLocalDate(),
                                    rs.getLong("price")
                            )
                    )
            );
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
