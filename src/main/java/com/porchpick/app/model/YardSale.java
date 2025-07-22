package com.porchpick.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "yard_sales")
public class YardSale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private OffsetDateTime endTime;

    /*
    ====================================================================================================
    @JoinTable and @JoinColumn Explained for Many-to-Many Relationships
    ====================================================================================================

    This is the "owning" side of the many-to-many relationship between YardSale and Item.
    We use @JoinTable to tell JPA how to create and manage the "join table" (also called a "link" or
    "association" table) that connects YardSales and Items.

    What the Join Table Looks Like:
    -------------------------------
    The @JoinTable annotation below will create a new table in the database named `yard_sale_items`.
    This table will have two columns:
    1. yard_sale_id: This column will store the primary key (ID) of a YardSale.
    2. item_id:       This column will store the primary key (ID) of an Item.

    Each row in this table represents a single link. For example, if YardSale 'A' contains Item 'X' and Item 'Y',
    the `yard_sale_items` table would have two rows:
    +----------------+---------+
    | yard_sale_id   | item_id |
    +----------------+---------+
    | A's ID         | X's ID  |
    | A's ID         | Y's ID  |
    +----------------+---------+

    Annotation Breakdown:
    ---------------------
    @JoinTable(name = "yard_sale_items", ...):
    - `name`: This specifies the name of the join table itself.

    joinColumns = @JoinColumn(name = "yard_sale_id"), ...:
    - `joinColumns`: This defines the foreign key column in the join table that refers back to the
                     "owning" entity (in this case, YardSale).
    - `@JoinColumn(name = "yard_sale_id")`: Specifies that the column name in the `yard_sale_items` table
                                           that holds the YardSale's ID will be `yard_sale_id`.

    inverseJoinColumns = @JoinColumn(name = "item_id")):
    - `inverseJoinColumns`: This defines the foreign key column in the join table that refers to the
                            "non-owning" or "inverse" entity (in this case, Item).
    - `@JoinColumn(name = "item_id")`: Specifies that the column name in the `yard_sale_items` table
                                      that holds the Item's ID will be `item_id`.
    */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "yard_sale_items",
        joinColumns = @JoinColumn(name = "yard_sale_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();


    /*
    ====================================================================================================
    @ManagedReference, Helper Methods, and Where to Put Them
    ====================================================================================================

    The `@ManagedReference` and `@JsonManagedReference` annotations are primarily used to solve a specific
    problem: infinite recursion during JSON serialization in bidirectional one-to-many or one-to-one relationships.
    They are generally NOT needed for many-to-many relationships. Why? Because the relationship is managed
    through a separate join table, which naturally breaks the circular reference that causes serialization loops.
    So, for this many-to-many relationship, we don't need `@ManagedReference`.

    Where should helper methods like `addItem` go?
    -------------------------------------------------
    The best practice is to put the helper methods on the "owning" side of the relationship. In this case,
    `YardSale` is the owning side because it has the `@JoinTable` annotation, which defines how the
    relationship is managed in the database.

    By placing `addItem` and `removeItem` here in the `YardSale` entity, we create a single, clear
    point of control for modifying the relationship. It ensures that the relationship is always
    kept in sync from the controlling side.

    Why not put it in both?
    ------------------------
    Putting synchronization logic in both entities (`YardSale` and `Item`) would create confusion and
    increase the risk of bugs. It would be unclear which side is responsible for managing the state,

    and you could easily end up with inconsistent code. The rule is simple: the owning side manages
    the relationship.
    */

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
} 