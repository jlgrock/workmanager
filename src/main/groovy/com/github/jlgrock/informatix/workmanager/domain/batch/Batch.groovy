package com.github.jlgrock.informatix.workmanager.domain.batch

import com.github.jlgrock.informatix.workmanager.domain.attachment.Attachment
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.*
import javax.validation.constraints.NotNull
/**
 *
 */
@Entity
@Table(name="batches")
@ToString
@EqualsAndHashCode
class Batch {

    @Id
    @NotNull
    @GeneratedValue
    Integer id

    @Column
    String name

    @Column(name="num_records", nullable = false)
    int numberOfRecords

    @OneToOne(targetEntity = Attachment.class, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "original_attachment_id", nullable = false)
    Attachment original

    @Column(name="num_unvalidated_records", nullable = false)
    int numberOfUnvalidatedRecords

    @OneToOne(targetEntity = Attachment.class, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_validation_attachment_id", nullable = false)
    Attachment postValidation

    @OneToOne(targetEntity = Attachment.class, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "compiled_attachment_id", nullable = false)
    Attachment compiled

    @OneToMany(targetEntity = Attachment.class, cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name="batches_reviewed",
            joinColumns = @JoinColumn(name = "batch_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id", referencedColumnName = "id", unique = true)
    )
    Collection<Attachment> reviewed

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    BatchStatus status

}
