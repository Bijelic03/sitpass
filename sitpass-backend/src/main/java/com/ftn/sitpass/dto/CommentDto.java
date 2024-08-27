package com.ftn.sitpass.dto;

import com.ftn.sitpass.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private Long authorId;

    private String text;

    private LocalDateTime createdAt;

    private String authorName;

    public static CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .authorName(comment.getAuthor().getName())
                .build();

    }

    public Comment convertToModel() {
        return Comment.builder()
                .id(getId())
                .text(getText())
                .createdAt(getCreatedAt())
                .build();
    }
}
