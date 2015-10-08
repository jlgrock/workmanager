package com.github.jlgrock.informatix.workmanager.domain.attachment
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.http.MediaType

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.LocalDateTime
/**
 *
 */
@Entity
@Table(name = "attachments")
@ToString
@EqualsAndHashCode
class Attachment {

    @Id
    @NotNull
    @GeneratedValue
    Integer id

    @NotEmpty
    @Column(name = "filename", nullable = false)
    String fileName

    @NotNull
    @Column(name = "upload_date", nullable = false)
    LocalDateTime uploadDate

    @NotNull
    @Column(name = "num_bytes", nullable = false)
    int numberOfBytes

    @NotNull
    @Column(name= "media_type", nullable = false)
    MediaType mediaType

    @NotNull
    @Size(min = 0)
    @Lob
    @Column(name = "data", nullable = false)
    byte[] data

}
