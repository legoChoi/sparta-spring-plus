package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class TodoQRepositoryImpl implements TodoQRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Todo> findByIdWithUser(Long todoId) {

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(todo)
                        .innerJoin(todo.user, user)
                        .fetchJoin()
                        .where(todo.id.eq(todoId))
                        .fetchOne());
    }

    @Override
    public Page<TodoSearchResponse> search(String title, String nickname, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        // title, nickname 부분 일치 가능
        // 생성일 범위 start~end

        // 조회 컬럼들 : 일정 제목, 담당자 수, 댓글 수
        // 첫번째 트라이: group by 사용한 집계 함수로 담당자 수, 댓글 수 집계

        List<TodoSearchResponse> records = queryFactory
                .select(
                        Projections.constructor(
                                TodoSearchResponse.class,
                                todo.title,
                                manager.id.countDistinct().as("managerCount"),
                                comment.id.countDistinct().as("commentCount")
                        )
                )
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .leftJoin(todo.comments, comment)
                .where(
                        addTitleCondition(title),
                        addManagerNicknameCondition(nickname),
                        addStartDateCondition(startDate),
                        addEndDateCondition(endDate)
                )
                .orderBy(todo.createdAt.desc())
                .groupBy(todo.id, todo.title)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        Long totalCount = queryFactory
                .select(Wildcard.count)
                .from(todo)
                .where(
                        addTitleCondition(title),
                        addManagerNicknameCondition(nickname),
                        addStartDateCondition(startDate),
                        addEndDateCondition(endDate)
                )
                .fetchOne();

        return PageableExecutionUtils.getPage(records, pageable, () -> totalCount);
    }

    private BooleanExpression addTitleCondition(String title) {
        return Objects.nonNull(title) ? todo.title.contains(title) : null;
    }

    private BooleanExpression addManagerNicknameCondition(String nickname) {
        return Objects.nonNull(nickname) ? manager.user.nickname.contains(nickname) : null;
    }

    private BooleanExpression addStartDateCondition(LocalDateTime startDate) {
        return Objects.nonNull(startDate) ? todo.createdAt.after(startDate) : null;
    }

    private BooleanExpression addEndDateCondition(LocalDateTime endDate) {
        return Objects.nonNull(endDate) ? todo.createdAt.before(endDate) : null;
    }
}
