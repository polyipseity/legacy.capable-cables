package io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.utilities.collections;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nonnull;
import io.github.etaoinshrdlcumwfgypbvkjxqz.capablecables.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ListBackedDeque<E>
		implements Deque<E> {
	private final List<E> data;
	private final List<E> reverseData;

	public ListBackedDeque(Supplier<? extends List<E>> listConstructor) {
		this(iterable -> listConstructor.get(), ImmutableList.of());
	}

	public ListBackedDeque(Function<? super Iterable<? extends E>, ? extends List<E>> listConstructor, Iterable<? extends E> data) {
		this.data = listConstructor.apply(data);
		this.reverseData = Lists.reverse(this.data);
	}

	public static <E> ListBackedDeque<E> ofImmutable(Iterable<? extends E> data) {
		return new ListBackedDeque<>(ImmutableList::copyOf, data);
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<E> getData() {
		return data;
	}

	@SuppressWarnings("AssignmentOrReturnOfFieldWithMutableType")
	protected List<E> getReverseData() {
		return reverseData;
	}

	@Override
	public void addFirst(E e) {
		getData().add(0, e);
	}

	@Override
	public void addLast(E e) {
		getData().add(e);
	}

	@Override
	public boolean offerFirst(E e) {
		addFirst(e); // COMMENT lists do not have capacity constraints
		return true;
	}

	@Override
	public boolean offerLast(E e) {
		addLast(e); // COMMENT lists do not have capacity constraints
		return true;
	}

	@Override
	public E removeFirst() {
		if (isEmpty())
			throw new NoSuchElementException();
		return getData().remove(0);
	}

	@Override
	public E removeLast() {
		if (isEmpty())
			throw new NoSuchElementException();
		return getData().remove(size() - 1);
	}

	@Override
	public E pollFirst() {
		return isEmpty() ? null : getData().remove(0);
	}

	@Override
	public E pollLast() {
		return isEmpty() ? null : getData().remove(size() - 1);
	}

	@Override
	public E getFirst() {
		if (isEmpty())
			throw new NoSuchElementException();
		return getData().get(0);
	}

	@Override
	public E getLast() {
		if (isEmpty())
			throw new NoSuchElementException();
		return getData().get(size() - 1);
	}

	@Override
	public E peekFirst() {
		return isEmpty() ? null : getData().get(0);
	}

	@Override
	public E peekLast() {
		return isEmpty() ? null : getData().get(size() - 1);
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	@Override
	public boolean removeFirstOccurrence(Object o) {
		return getData().remove(o);
	}

	@SuppressWarnings("SuspiciousMethodCalls")
	@Override
	public boolean removeLastOccurrence(Object o) {
		return getReverseData().remove(o);
	}

	@Override
	public boolean add(E e) {
		addLast(e);
		return true;
	}

	@Override
	public boolean offer(E e) {
		return offerLast(e);
	}

	@Override
	public E remove() {
		return removeFirst();
	}

	@Override
	public E poll() {
		return pollFirst();
	}

	@Nullable
	@Override
	public E element() {
		return getFirst();
	}

	@Nullable
	@Override
	public E peek() {
		return peekFirst();
	}

	@Override
	public void push(E e) {
		addFirst(e);
	}

	@Override
	public E pop() {
		return removeFirst();
	}

	@Override
	public boolean remove(Object o) {
		return removeFirstOccurrence(o);
	}

	@Override
	public boolean containsAll(@Nonnull Collection<?> c) {
		return getData().containsAll(c);
	}

	@Override
	public boolean addAll(@Nonnull Collection<? extends E> c) {
		return getData().addAll(c);
	}

	@Override
	public boolean removeAll(@Nonnull Collection<?> c) {
		return getData().removeAll(c);
	}

	@Override
	public boolean retainAll(@Nonnull Collection<?> c) {
		return getData().retainAll(c);
	}

	@Override
	public void clear() {
		getData().clear();
	}

	@Override
	public boolean contains(Object o) {
		return getData().contains(o);
	}

	@Override
	public int size() {
		return getData().size();
	}

	@Override
	public boolean isEmpty() {
		return getData().isEmpty();
	}

	@Override
	public @Nonnull Iterator<E> iterator() {
		return getData().iterator();
	}

	@Nonnull
	@Override
	public Object[] toArray() {
		return getData().toArray();
	}

	@SuppressWarnings("SuspiciousToArrayCall")
	@Nonnull
	@Override
	public <T> T[] toArray(@Nonnull T[] a) {
		return getData().toArray(a);
	}

	@Nonnull
	@Override
	public Iterator<E> descendingIterator() {
		return getReverseData().iterator();
	}
}
