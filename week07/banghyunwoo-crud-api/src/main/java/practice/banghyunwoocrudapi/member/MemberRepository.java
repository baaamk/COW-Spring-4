package practice.banghyunwoocrudapi.member;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class MemberRepository {

    //name은 unique 해야한다.
    private final Map<String, Member> memberRepository = new ConcurrentHashMap<>();

    public void save(Member member) {
        memberRepository.put(member.getId(), member);
    }

    public Member findById(String id) {
        return memberRepository.get(id);
    }

    public void delete(String id) {
        memberRepository.remove(id);
    }
}
