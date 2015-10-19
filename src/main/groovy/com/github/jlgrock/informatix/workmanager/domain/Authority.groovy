package com.github.jlgrock.informatix.workmanager.domain

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "authorities")
@ToString
@EqualsAndHashCode
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
class Authority implements Serializable {

    @NotNull
    @Size(min = 0, max = 50)
    @Id
    @Column(name="name", length = 50)
    String name

}
